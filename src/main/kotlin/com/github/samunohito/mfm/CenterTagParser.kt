package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmCenter

class CenterTagParser : IParser<MfmCenter> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmCenter> {
    return ParserResult.ofFailure()
  }
}