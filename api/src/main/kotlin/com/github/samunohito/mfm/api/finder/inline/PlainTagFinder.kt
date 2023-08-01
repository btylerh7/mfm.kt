package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

/**
 * An [ISubstringFinder] implementation for detecting "plain tag" syntax.
 * The string enclosed by <plain> tags will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 */
object PlainTagFinder : ISubstringFinder {
  private val open = StringFinder("<plain>")
  private val close = StringFinder("</plain>")
  private val plainTagFinder = SequentialFinder(
    open,
    NewLineFinder.optional(),
    SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
    NewLineFinder.optional(),
    close,
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = plainTagFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[2]
    return success(
      FoundType.PlainTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}