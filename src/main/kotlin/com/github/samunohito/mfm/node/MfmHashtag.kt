package com.github.samunohito.mfm.node

data class MfmHashtag(override val props: Props) : IMfmInline, IMfmNodePropertyHolder<MfmHashtag.Props> {
  override val type = MfmNodeType.HashTag

  constructor(hashtag: String) : this(Props(hashtag))

  data class Props(val hashtag: String) : IMfmProps
}