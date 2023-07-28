package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.MfmStrike
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

class StrikeTagNodeFactory : SimpleNodeFactoryBase<MfmStrike>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.StrikeTag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmStrike> {
    val result = NodeFactoryUtils.createNodes(input, foundInfo.sub, MfmNodeAttribute.setOfInline, context)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmStrike(result), foundInfo)
  }
}