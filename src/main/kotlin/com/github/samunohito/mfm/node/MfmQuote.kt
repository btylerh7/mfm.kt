package com.github.samunohito.mfm.node

data class MfmQuote(
  override val children: List<IMfmNode>
) : IMfmBlock, IMfmNodeNestable<MfmQuote> {
  override val type = MfmNodeType.Quote

  override fun addChild(nodes: Iterable<IMfmNode>): MfmQuote {
    return copy(children = children + nodes)
  }
}