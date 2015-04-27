package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("decode and quick encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, quickEncode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("times character appears in string") {
    val actual = times(string2Chars("abca")).toSet
    val expected = Set(('a', 2),('b', 1),('c', 1))
    assert(actual === expected)
  }

  test("test for singleton tree") {
    assert(singleton(List()) === false)
    assert(singleton(List(Leaf('a', 3))) === true)
    assert(singleton(List(Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5))) === true)
    assert(singleton(List(Leaf('a', 3), Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5))) === false)
  }

  test("convert tree") {
    new TestTrees {
      assert(convert(t1) === List(('a', List(0)), ('b', List(1))))
    }
  }

  test("quick encode") {
    new TestTrees {
      assert(quickEncode(t1)(string2Chars("ab")) === List(0, 1))
    }
  }

  test("createCodeTree") {
    assert(createCodeTree(string2Chars("ab")) === Fork(Leaf('a', 1), Leaf('b',1), List('a','b'), 2)
    )
  }
}
