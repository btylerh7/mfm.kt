package com.github.samunohito.mfm.node

abstract class MfmNodeBase : IMfmNode {
  override fun equals(other: Any?): Boolean {
    return if (other is IMfmNode) {
      nodeEquals(this, other)
    } else {
      false
    }
  }

  override fun hashCode(): Int {
    return nodeHashCode(this)
  }

  companion object {
    private fun nodeEquals(x: IMfmNode, y: IMfmNode): Boolean {
      if (x === y) return true
      if (x.javaClass != y.javaClass) return false
      if (x is IMfmNodePropertyHolder<*> && y is IMfmNodePropertyHolder<*>) {
        if (x.props != y.props) return false
      }
      if (x is IMfmNodeChildrenHolder && y is IMfmNodeChildrenHolder) {
        if (x.children != y.children) return false
      }
      return true
    }

    private fun nodeHashCode(x: IMfmNode): Int {
      var result = x.javaClass.hashCode()
      if (x is IMfmNodePropertyHolder<*>) {
        result = 31 * result + x.props.hashCode()
      }
      if (x is IMfmNodeChildrenHolder) {
        result = 31 * result + x.children.hashCode()
      }
      return result
    }
  }
}