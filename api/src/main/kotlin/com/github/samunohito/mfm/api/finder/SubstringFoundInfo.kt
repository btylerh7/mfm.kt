package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType

data class SubstringFoundInfo(
  val type: FoundType,
  val fullRange: IntRange,
  val contentRange: IntRange,
  val next: Int,
  val sub: List<SubstringFoundInfo> = listOf(),
) {
  companion object {
    val EMPTY = SubstringFoundInfo(FoundType.Empty, IntRange.EMPTY, IntRange.EMPTY, Int.MIN_VALUE, emptyList())
  }

  constructor(type: FoundType, info: SubstringFoundInfo) : this(
    type,
    info.fullRange,
    info.contentRange,
    info.next,
    info.sub
  )

  operator fun get(index: Int): SubstringFoundInfo {
    return sub[index]
  }

  operator fun get(index: ISubIndex): SubstringFoundInfo {
    return sub[index.index]
  }
}

interface ISubIndex {
  val index: Int
}