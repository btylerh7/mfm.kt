package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmEmojiCode

object EmojiCodeNodeFactory : SimpleNodeFactoryBase<MfmEmojiCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.EmojiCode)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmEmojiCode> {
    return success(MfmEmojiCode(input.substring(foundInfo.contentRange)), foundInfo)
  }
}