package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmBold
import com.github.samunohito.mfm.node.MfmText

class BoldAstaParser : SimpleParserBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldAsta)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmBold> {
    val contents = input.substring(foundInfo.range)
    val textNode = MfmText(contents)
    return success(MfmBold(listOf(textNode)), foundInfo)
  }
}