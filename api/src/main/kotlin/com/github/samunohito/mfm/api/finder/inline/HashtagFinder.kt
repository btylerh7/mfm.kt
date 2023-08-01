package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.*
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

/**
 * An [ISubstringFinder] implementation for detecting "hashtag" syntax.
 * Strings starting with "#" will be search results.
 *
 * ### Notes
 * - The content cannot be left empty.
 * - The content cannot contain half-width spaces, full-width spaces, newlines, or tab characters.
 * - The content cannot include . , ! ? ' " # : / 【 】 < > 【 】 ( ) 「 」 （ ）.
 * - Parentheses can only be included in the content when they come in pairs. Relevant pairs: () [] 「」 （）.
 * - Recognize as a hashtag if the character before # does not match [a-z0-9](case-insensitive).
 * - If the content is only numbers, it will not be recognized as a hashtag.
 */
object HashtagFinder : ISubstringFinder {
  private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
  private val regexNumericOnly = Regex("^[0-9]+$")
  private val mark = StringFinder("#")
  private val hashtagFinder = SequentialFinder(mark, HashtagBodyFinder)

  private object HashtagBodyFinder : ISubstringFinder {
    private val openRegexBracket = RegexFinder(Regex("[(\\[「（]"))
    private val closeRegexBracket = RegexFinder(Regex("[)\\]」）]"))
    private val nestableFinder = AlternateFinder(
      // パターンにカッコを含めると、終了カッコのみを誤検出してしまう。
      // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
      SequentialFinder(
        openRegexBracket,
        HashtagBodyFinder,
        closeRegexBracket,
      ),
      // このパターンに合致する文字が登場するまでを繰り返し検索する
      AlternateScanFinder.ofUntil(
        SpaceFinder,
        NewLineFinder,
        RegexFinder(Regex("[ \\u3000\\t.,!?'\"#:/\\[\\]【】()「」（）<>]")),
      )
    )

    override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
      var latestIndex = startAt
      val foundInfos = mutableListOf<SubstringFoundInfo>()

      while (true) {
        val result = nestableFinder.find(input, latestIndex, context)
        if (!result.success) {
          break
        }

        foundInfos.add(result.foundInfo)
        latestIndex = result.foundInfo.resumeIndex
      }

      if (foundInfos.isEmpty()) {
        return failure()
      }

      val fullRange = startAt until latestIndex
      val contentRange = foundInfos.map { it.contentRange }.merge()
      return success(FoundType.Hashtag, fullRange, contentRange, fullRange.next(), foundInfos)
    }
  }

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = hashtagFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    // ハッシュタグの直前が英数字の場合はハッシュタグとして認識しない
    val beforeStr = input.substring(0 until startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    // 検出された文字が数値のみの場合はハッシュタグではない
    val hashtagNameResult = result.foundInfo.nestedInfos[1]
    val hashtagName = input.substring(hashtagNameResult.contentRange)
    if (regexNumericOnly.containsMatchIn(hashtagName)) {
      return failure()
    }

    return success(
      FoundType.Hashtag,
      result.foundInfo.overallRange,
      hashtagNameResult.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}