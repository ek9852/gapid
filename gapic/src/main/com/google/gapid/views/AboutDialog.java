/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gapid.views;

import static com.google.gapid.widgets.Widgets.centered;
import static com.google.gapid.widgets.Widgets.createComposite;
import static com.google.gapid.widgets.Widgets.createLabel;
import static java.util.logging.Level.SEVERE;

import com.google.gapid.Version;
import com.google.gapid.models.Info;
import com.google.gapid.util.Logging;
import com.google.gapid.util.Messages;
import com.google.gapid.util.OS;
import com.google.gapid.widgets.DialogBase;
import com.google.gapid.widgets.Theme;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Dialog showing some basic info about our application.
 */
public class AboutDialog {
  private static final String HELP_URL =
      "https://developer.android.com/r/studio-ui/am-gpu-debugger.html";
  private static final Logger LOG = Logger.getLogger(AboutDialog.class.getName());

  private AboutDialog() {
  }

  public static void showHelp() {
    Program.launch(HELP_URL);
  }

  public static void showLogDir() {
    try {
      OS.openFileInSystemExplorer(Logging.getLogDir());
    } catch (IOException e) {
      LOG.log(SEVERE, "Failed to open log directory in system explorer", e);
    }
  }

  public static void showAbout(Shell shell, Theme theme) {
    new DialogBase(shell) {
      @Override
      public String getTitle() {
        return Messages.ABOUT_TITLE;
      }

      @Override
      protected Control createDialogArea(Composite parent) {
        Composite area = (Composite)super.createDialogArea(parent);
        area.setBackground(theme.aboutBackground());

        Composite container = createComposite(area, centered(new RowLayout(SWT.VERTICAL)));
        container.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));

        container.setBackground(theme.aboutBackground());
        createLabel(container, "", theme.logoBig());
        Label title = createForegroundLabel(container, Messages.WINDOW_TITLE);
        title.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
        createForegroundLabel(container, "Version " + Version.GAPID_VERSION);
        createForegroundLabel(
            container, "Server: " + Info.getServerName() + ", Version: " + Info.getServerVersion());
        createForegroundLabel(container, Messages.ABOUT_DESCRIPTION);
        createForegroundLabel(container, Messages.ABOUT_COPY);

        return area;
      }

      @Override
      protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
      }

      public Label createForegroundLabel(Composite parent, String text) {
        Label label = createLabel(parent, text);
        label.setForeground(theme.aboutForeground());
        label.setBackground(theme.aboutBackground());
        return label;
      }

    }.open();
  }
}
