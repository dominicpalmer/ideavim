/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.ex.parser.statements

import com.maddyhome.idea.vim.vimscript.model.commands.EchoCommand
import com.maddyhome.idea.vim.vimscript.model.expressions.Scope
import com.maddyhome.idea.vim.vimscript.model.statements.FunctionDeclaration
import com.maddyhome.idea.vim.vimscript.model.statements.FunctionFlag
import com.maddyhome.idea.vim.vimscript.model.statements.ReturnStatement
import com.maddyhome.idea.vim.vimscript.parser.VimscriptParser
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.FromDataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(Theories::class)
class FunctionDeclarationTests {

  companion object {
    @JvmStatic
    val spaces = listOf("", " ")
      @DataPoints("spaces") get

    @JvmStatic
    val functionAlias = listOf("fu", "fun", "func", "funct", "functi", "functio", "function")
      @DataPoints("function") get

    @JvmStatic
    val endfunctionAlias =
      listOf("endf", "endfu", "endfun", "endfunc", "endfunct", "endfuncti", "endfunctio", "endfunction")
      @DataPoints("endfunction") get

    @JvmStatic
    val flagAlias = listOf("range", "abort", "dict", "closure")
      @DataPoints("flags") get
  }

  @Theory
  fun `function with no arguments`(
    @FromDataPoints("spaces") sp1: String,
    @FromDataPoints("spaces") sp2: String,
    @FromDataPoints("spaces") sp3: String,
    @FromDataPoints("spaces") sp4: String,
    @FromDataPoints("spaces") sp5: String,
  ) {
    val script = VimscriptParser.parse(
      """
        function helloWorld$sp1($sp2)$sp3
            echo 'hello world'$sp4
        endfunction$sp5
      """.trimIndent()
    )
    assertEquals(1, script.units.size)
    assertTrue(script.units[0] is FunctionDeclaration)
    val f = script.units[0] as FunctionDeclaration
    assertNull(f.scope)
    assertEquals("helloWorld", f.name)
    assertEquals(0, f.args.size)
    assertEquals(1, f.body.size)
    assertFalse(f.replaceExisting)
    assertTrue(f.body[0] is EchoCommand)
  }

  @Theory
  fun `function with arguments and replace flag`(
    @FromDataPoints("spaces") sp1: String,
    @FromDataPoints("spaces") sp2: String,
    @FromDataPoints("spaces") sp3: String,
    @FromDataPoints("spaces") sp4: String,
    @FromDataPoints("spaces") sp5: String,
    @FromDataPoints("spaces") sp6: String,
    @FromDataPoints("spaces") sp7: String,
    @FromDataPoints("spaces") sp8: String,
    @FromDataPoints("spaces") sp9: String,
  ) {
    val script = VimscriptParser.parse(
      """
        " prefix with s: for local script-only functions
        function! s:Initialize$sp1(${sp2}cmd$sp3,${sp4}args$sp5)$sp6
            " a: prefix for arguments
            echo "Command: " . a:cmd
            $sp7
            return 'true'$sp8
        endfunction$sp9
      """.trimIndent()
    )
    assertEquals(1, script.units.size)
    assertTrue(script.units[0] is FunctionDeclaration)
    val f = script.units[0] as FunctionDeclaration
    assertEquals(Scope.SCRIPT_VARIABLE, f.scope)
    assertEquals("Initialize", f.name)
    assertEquals(listOf("cmd", "args"), f.args)
    assertEquals(2, f.body.size)
    assertTrue(f.replaceExisting)
    assertTrue(f.body[0] is EchoCommand)
    assertTrue(f.body[1] is ReturnStatement)
  }

  @Theory
  fun `function keyword test`(
    @FromDataPoints("function") functionAlias: String,
    @FromDataPoints("endfunction") endfunctionAlias: String,
    @FromDataPoints("spaces") sp1: String,
    @FromDataPoints("spaces") sp2: String,
  ) {
    val script = VimscriptParser.parse(
      """
        $functionAlias F1()$sp1
        $endfunctionAlias$sp2
      """.trimIndent()
    )
    assertEquals(1, script.units.size)
    assertTrue(script.units[0] is FunctionDeclaration)
  }

  @Theory
  fun `function flag test`(
    @FromDataPoints("flags") flag1: String,
    @FromDataPoints("spaces") sp1: String,
    @FromDataPoints("spaces") sp2: String,
    @FromDataPoints("spaces") sp3: String,
  ) {
    val script = VimscriptParser.parse(
      """
        fun F1()$sp1$flag1$sp2
        endf$sp3
      """.trimIndent()
    )
    assertEquals(1, script.units.size)
    assertTrue(script.units[0] is FunctionDeclaration)
    val f = script.units[0] as FunctionDeclaration
    assertEquals(f.flags, setOf(FunctionFlag.getByName(flag1)))
  }

  @Theory
  fun `function with multiple flags test`(
    @FromDataPoints("flags") flag1: String,
    @FromDataPoints("flags") flag2: String,
    @FromDataPoints("spaces") sp1: String,
    @FromDataPoints("spaces") sp2: String,
    @FromDataPoints("spaces") sp3: String,
  ) {
    val script = VimscriptParser.parse(
      """
        fun F1()$sp1$flag1 $flag2$sp2
        endf$sp3
      """.trimIndent()
    )
    assertEquals(1, script.units.size)
    assertTrue(script.units[0] is FunctionDeclaration)
    val f = script.units[0] as FunctionDeclaration
    assertEquals(f.flags, setOf(FunctionFlag.getByName(flag1), FunctionFlag.getByName(flag2)))
  }

  @Theory
  fun `dictionary function`() {
    val script = VimscriptParser.parse(
      """
        " prefix with s: for local script-only functions
        function! s:dict.something.Initialize()
            return 'true'
        endfunction
      """.trimIndent()
    )
    assertEquals(1, script.units.size)
  }

//  // https://youtrack.jetbrains.com/issue/VIM-2654
//  @Theory
//  fun `return with omitted expression`() {
//    VimscriptParser.parse(
//      """
//        func! Paste_on_off()
//           if g:paste_mode == 0
//              set paste
//              let g:paste_mode = 1
//           else
//              set nopaste
//              let g:paste_mode = 0
//           endif
//           return
//        endfunc
//      """.trimIndent()
//    )
//    assertEmpty(IdeavimErrorListener.testLogger)
//  }
}
