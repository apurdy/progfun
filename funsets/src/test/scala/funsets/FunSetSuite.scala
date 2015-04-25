package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val sNegInts = {x: Int => x < 0}
    val sPosInts = {x: Int => x > 0}
    val sGreaterThanTen = {x: Int => x > 10}
    val sEvens = {x: Int => x % 2 == 0}
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("contains") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(contains(sNegInts, -1), "negative ints contains - 1")
      assert(!contains(sNegInts, 0), "negative ints doesn't contain 0")
    }
  }

  test("intersect") {
    new TestSets {
      assert(!contains(intersect(sNegInts, sPosInts), 0), "intersect pos neg")
      assert(!contains(intersect(sGreaterThanTen, sPosInts), 3), "pos ints intersect gt 10 doesn't contain 3")
    }
  }

  test("union") {
    new TestSets {
      assert(contains(union(sNegInts, sPosInts), -1), "union pos neg contains -1")
      assert(contains(union(sNegInts, sPosInts), 1), "union pos neg contains +1")
      assert(contains(union(sGreaterThanTen, sPosInts), 3), "pos ints union gt 10 contains 3")
    }
  }


  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }


  test("filter even numbers") {
    new TestSets {
      val s = filter(sGreaterThanTen, { x: Int => x % 2 == 0 })
      assert(contains(s, 12), "filter contains even")
      assert(!contains(s, 11), "filter doesn't contains odd")
    }
  }

  test("diff even numbers") {
    new TestSets {
      val s = diff(sGreaterThanTen, sEvens)
      assert(contains(s, 11), "diff contains odd")
      assert(!contains(s, 12), "filter doesn't contain evens")
    }
  }

  test("forall") {
    new TestSets {
      assert(forall(sGreaterThanTen, {p: Int => p > 10}), "set def")
    }
  }

  test("exists") {
    new TestSets {
      assert(!exists(sGreaterThanTen, {p: Int => p == 9}), "values less than 10 fail existence test")
    }
  }

  test("map") {
    new TestSets {
      val m = map(s1, x => x * 2)
      assert(contains(m, 2), "m should contain 2")
      assert(!contains(m, 1), "m should not contain 1")
    }
  }
}
