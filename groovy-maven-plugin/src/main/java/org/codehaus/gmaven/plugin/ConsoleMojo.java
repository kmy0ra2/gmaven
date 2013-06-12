/*
 * Copyright (c) 2007-2013, the original author or authors.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package org.codehaus.gmaven.plugin;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.gmaven.adapter.ConsoleWindow;
import org.codehaus.gmaven.adapter.ConsoleWindow.WindowHandle;
import org.codehaus.gmaven.adapter.ResourceLoader;
import org.codehaus.gmaven.plugin.util.SystemNoExitGuard;

/**
 * Open the Groovy console.
 *
 * @since 2.0
 */
@Mojo(name = "console", aggregator = true)
public class ConsoleMojo
    extends RuntimeMojoSupport
{
  @Override
  protected void run() throws Exception {
    final ResourceLoader resourceLoader = new MojoResourceLoader(runtimeRealm, null, scriptpath);
    final Map<String, Object> context = createContext();
    final ConsoleWindow console = runtime.getConsoleWindow();

    // open console window guarding against system exist and protecting system streams
    new SystemNoExitGuard().run(new Callable<Void>()
    {
      @Override
      public Void call() throws Exception {
        WindowHandle handle = console.open(runtimeRealm, resourceLoader, context);
        handle.await();
        return null;
      }
    });
  }
}
