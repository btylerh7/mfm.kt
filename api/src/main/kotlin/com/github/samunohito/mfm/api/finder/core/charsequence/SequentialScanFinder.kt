package com.github.samunohito.mfm.api.finder.core.charsequence

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

class SequentialScanFinder private constructor(
  private val terminates: Collection<ISubstringFinder>
) : ScanFinderBase() {
  override fun hasNext(text: String, startAt: Int, context: ISubstringFinderContext): Boolean {
    val result = SubstringFinderUtils.sequential(text, startAt, terminates, context)
    return !result.success
  }

  companion object {
    fun ofUntil(terminates: Collection<ISubstringFinder>): SequentialScanFinder {
      return SequentialScanFinder(terminates)
    }

    fun ofUntil(vararg terminates: ISubstringFinder): SequentialScanFinder {
      return SequentialScanFinder(terminates.toList())
    }
  }
}