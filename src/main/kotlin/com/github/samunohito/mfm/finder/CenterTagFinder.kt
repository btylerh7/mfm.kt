package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder

class CenterTagFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("<center>")
    private val close = StringFinder("</center>")
    private val finder = SequentialFinder(
      NewLineFinder.optional(),
      LineBeginFinder,
      open,
      NewLineFinder.optional(),
      InlineFinder(SequentialFinder(NewLineFinder, close)),
      NewLineFinder.optional(),
      close,
      LineEndFinder,
      NewLineFinder.optional(),
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[4]
    return success(FoundType.CenterTag, contents.range, result.foundInfo.next)
  }
}