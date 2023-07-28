package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType

interface ISubstringFinderResult {
  val success: Boolean
  val foundInfo: SubstringFoundInfo
}

private data class Impl(
  override val success: Boolean,
  override val foundInfo: SubstringFoundInfo,
) : ISubstringFinderResult {
  companion object {
    @JvmStatic
    fun ofSuccess(result: SubstringFoundInfo): ISubstringFinderResult {
      return Impl(true, result)
    }

    @JvmStatic
    fun ofFailure(): ISubstringFinderResult {
      return Impl(false, SubstringFoundInfo.EMPTY)
    }
  }
}

fun success(result: SubstringFoundInfo): ISubstringFinderResult {
  return Impl.ofSuccess(result)
}

fun success(
  foundType: FoundType,
  fullRange: IntRange,
  contentRange: IntRange,
  next: Int,
  sub: List<SubstringFoundInfo> = listOf()
): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, fullRange, contentRange, next, sub))
}

fun success(foundType: FoundType, result: ISubstringFinderResult): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, result.foundInfo))
}

fun success(foundType: FoundType, info: SubstringFoundInfo): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, info))
}

fun failure(): ISubstringFinderResult {
  return Impl.ofFailure()
}