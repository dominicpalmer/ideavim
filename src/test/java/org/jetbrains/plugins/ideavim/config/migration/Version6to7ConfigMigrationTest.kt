/*
 * Copyright 2003-2023 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

@file:Suppress("ClassName")

package org.jetbrains.plugins.ideavim.config.migration

import com.intellij.openapi.util.JDOMUtil
import com.maddyhome.idea.vim.config.migration.`Version 6 to 7 config migration`
import org.intellij.lang.annotations.Language
import org.junit.Test
import kotlin.test.assertTrue

class `Version 6 to 7 config migration test` {
  @Test
  fun `simple migration`() {
    val initialConfig = JDOMUtil.load(testConfig)

    val (localElement, sharedElement) = `Version 6 to 7 config migration`.performMigration(initialConfig)

    assertTrue(JDOMUtil.areElementsEqual(JDOMUtil.load(localConfig), localElement))
    assertTrue(JDOMUtil.areElementsEqual(JDOMUtil.load(sharedConfig), sharedElement))
  }
}

@Language("xml")
internal val testConfig = """
  <application>
    <component name="VimEditorSettings">
      <editor />
    </component>
    <component name="VimHistorySettings">
      <history>
        <history-search>
          <entry encoding="base64">aGVsbG8=</entry>
        </history-search>
        <history-cmd>
          <entry>qa</entry>
          <entry>w</entry>
          <entry>ExchangeClear</entry>
        </history-cmd>
        <history-expr />
        <history-input />
      </history>
    </component>
    <component name="VimKeySettings">
      <shortcut-conflicts>
        <shortcut-conflict owner="vim">
          <text>ctrl pressed V</text>
        </shortcut-conflict>
        <shortcut-conflict owner="vim">
          <text>ctrl pressed A</text>
        </shortcut-conflict>
      </shortcut-conflicts>
    </component>
    <component name="VimMarksSettings">
      <globalmarks />
      <filemarks>
        <file name="/Dummy.txt" timestamp="1591167977880">
          <mark key="[" line="0" column="0" />
          <mark key="]" line="0" column="0" />
          <mark key="." line="0" column="0" />
          <mark key="^" line="0" column="18" />
        </file>
      </filemarks>
      <jumps>
        <jump line="190" column="0" filename="/myFile" />
      </jumps>
    </component>
    <component name="VimRegisterSettings">
      <registers>
        <register name="a" type="4">
          <text encoding="base64">aGVsbG8=</text>
        </register>
      </registers>
    </component>
    <component name="VimSearchSettings">
      <search>
        <last-search encoding="base64">aGVsbG8=</last-search>
        <last-offset />
        <last-pattern encoding="base64">aGVsbG8=</last-pattern>
        <last-replace>~</last-replace>
        <last-substitute encoding="base64">aGVsbG8=</last-substitute>
        <last-dir>1</last-dir>
        <show-last>false</show-last>
      </search>
    </component>
    <component name="VimSettings">
      <state version="6" enabled="true" />
      <notifications>
        <idea-join enabled="false" />
        <idea-put enabled="true" />
      </notifications>
    </component>
  </application>
""".trimIndent()

@Language("xml")
internal val localConfig = """
  <application>
    <component name="VimHistorySettings">
      <history>
        <history-search>
          <entry encoding="base64">aGVsbG8=</entry>
        </history-search>
        <history-cmd>
          <entry>qa</entry>
          <entry>w</entry>
          <entry>ExchangeClear</entry>
        </history-cmd>
        <history-expr />
        <history-input />
      </history>
    </component>
    <component name="VimMarksSettings">
      <globalmarks />
      <filemarks>
        <file name="/Dummy.txt" timestamp="1591167977880">
          <mark key="[" line="0" column="0" />
          <mark key="]" line="0" column="0" />
          <mark key="." line="0" column="0" />
          <mark key="^" line="0" column="18" />
        </file>
      </filemarks>
      <jumps>
        <jump line="190" column="0" filename="/myFile" />
      </jumps>
    </component>
    <component name="VimRegisterSettings">
      <registers>
        <register name="a" type="4">
          <text encoding="base64">aGVsbG8=</text>
        </register>
      </registers>
    </component>
    <component name="VimSearchSettings">
      <search>
        <last-search encoding="base64">aGVsbG8=</last-search>
        <last-offset />
        <last-pattern encoding="base64">aGVsbG8=</last-pattern>
        <last-replace>~</last-replace>
        <last-substitute encoding="base64">aGVsbG8=</last-substitute>
        <last-dir>1</last-dir>
        <show-last>false</show-last>
      </search>
    </component>
  </application>
""".trimIndent()

@Language("xml")
internal val sharedConfig = """
  <application>
    <component name="VimEditorSettings">
      <editor />
    </component>
    <component name="VimKeySettings">
      <shortcut-conflicts>
        <shortcut-conflict owner="vim">
          <text>ctrl pressed V</text>
        </shortcut-conflict>
        <shortcut-conflict owner="vim">
          <text>ctrl pressed A</text>
        </shortcut-conflict>
      </shortcut-conflicts>
    </component>
    <component name="VimSettings">
      <state version="6" enabled="true" />
      <notifications>
        <idea-join enabled="false" />
        <idea-put enabled="true" />
      </notifications>
    </component>
  </application>
""".trimIndent()
