package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder

@Suppress("DuplicatedCode")
class ItalicUnderFinder : ISubstringFinder {
  companion object {
    private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
    private val markFinder = StringFinder("_")
    private val italicUnderFinder = SequentialFinder(
      markFinder,
      AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
      markFinder
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = italicUnderFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    // 直前が英数字だったら認識しない
    val beforeStr = input.substring(0, startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(FoundType.ItalicUnder, result.foundInfo.fullRange, contents.contentRange, result.foundInfo.next)
  }
}