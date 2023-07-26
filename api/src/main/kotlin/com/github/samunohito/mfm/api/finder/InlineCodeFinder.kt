package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

class InlineCodeFinder : ISubstringFinder {
  companion object {
    private val mark = StringFinder("`")
    private val mark2 = StringFinder("´")
    private val inlineCodeFinder = SequentialFinder(
      mark,
      AlternateScanFinder.ofUntil(mark, mark2, NewLineFinder),
      mark
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = inlineCodeFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(FoundType.InlineCode, contents.range, result.foundInfo.next)
  }
}