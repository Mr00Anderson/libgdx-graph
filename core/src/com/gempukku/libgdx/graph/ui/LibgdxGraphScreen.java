package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.PipelineLoader;
import com.gempukku.libgdx.graph.ui.pipeline.PipelineDesignTab;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class LibgdxGraphScreen extends Table {
    private FileHandle editedFile;
    private final TabbedPane tabbedPane;
    private PipelineDesignTab pipelineDesignTab;
    private Skin skin;
    private final Table insideTable;

    public LibgdxGraphScreen(Skin skin) {
        this.skin = skin;
        setFillParent(true);
        insideTable = new Table(skin);

        tabbedPane = new TabbedPane();
        tabbedPane.addListener(
                new TabbedPaneAdapter() {
                    @Override
                    public void switchedTab(Tab tab) {
                        ((AwareTab) tabbedPane.getActiveTab()).sleep();
                        ((AwareTab) tabbedPane.getActiveTab()).awaken();

                        insideTable.clearChildren();
                        insideTable.add(tab.getContentTable()).grow().row();
                    }
                });

        MenuBar menuBar = createMenuBar();
        add(menuBar.getTable()).growX().row();
        add(tabbedPane.getTable()).left().growX().row();
        add(insideTable).grow().row();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = createFileMenu();

        menuBar.addMenu(fileMenu);
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newMenuItem = new MenuItem("New from template");
        newMenuItem.setSubMenu(createTemplateMenu());
        fileMenu.addItem(newMenuItem);

        MenuItem open = new MenuItem("Open");
        open.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        open();
                    }
                });
        fileMenu.addItem(open);

        MenuItem save = new MenuItem("Save");
        save.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.S);
        save.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        save();
                    }
                });
        fileMenu.addItem(save);

        MenuItem saveAs = new MenuItem("Save As");
        saveAs.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        saveAs();
                    }
                });
        fileMenu.addItem(saveAs);

        fileMenu.addSeparator();

        MenuItem close = new MenuItem("Close pipeline");
        close.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        closePipeline();
                    }
                });
        fileMenu.addItem(close);

        fileMenu.addSeparator();
        MenuItem exit = new MenuItem("Exit");
        exit.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        closeApplication();
                    }
                });
        fileMenu.addItem(exit);

        return fileMenu;
    }

    private void closeApplication() {
        if (pipelineDesignTab != null && pipelineDesignTab.isDirty()) {
            Dialogs.showOptionDialog(getStage(), "Pipeline modified",
                    "Current pipeline has been modified, would you like to save it?",
                    Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {
                        @Override
                        public void yes() {
                            save();
                            Gdx.app.exit();
                        }

                        @Override
                        public void no() {
                            Gdx.app.exit();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            Gdx.app.exit();
        }
    }

    private void closePipeline() {
        if (pipelineDesignTab != null && pipelineDesignTab.isDirty()) {
            Dialogs.showOptionDialog(getStage(), "Pipeline modified",
                    "Current pipeline has been modified, would you like to save it?",
                    Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {
                        @Override
                        public void yes() {
                            save();
                            removeAllTabs();
                        }

                        @Override
                        public void no() {
                            removeAllTabs();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            removeAllTabs();
        }
    }

    private void open() {
        if (pipelineDesignTab != null && pipelineDesignTab.isDirty()) {
            Dialogs.showErrorDialog(getStage(), "Current pipeline has been modified, close it or save it");
        } else {
            removeAllTabs();

            FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
            fileChooser.setModal(true);
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> file) {
                    FileHandle selectedFile = file.get(0);
                    loadTemplate(selectedFile);
                    editedFile = selectedFile;
                    pipelineDesignTab.setDirty(false);
                }
            });
            getStage().addActor(fileChooser.fadeIn());
        }
    }

    private void saveAs() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                    selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                }
                editedFile = selectedFile;
                saveToFile(pipelineDesignTab, selectedFile);
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void save() {
        if (pipelineDesignTab != null) {
            if (editedFile == null) {
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
                fileChooser.setModal(true);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        FileHandle selectedFile = file.get(0);
                        if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                            selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                        }
                        editedFile = selectedFile;
                        saveToFile(pipelineDesignTab, selectedFile);
                    }
                });
                getStage().addActor(fileChooser.fadeIn());
            } else {
                saveToFile(pipelineDesignTab, editedFile);
            }
        }
    }

    private void saveToFile(PipelineDesignTab pipelineDesignTab, FileHandle editedFile) {
        JSONObject renderer = new JSONObject();
        renderer.put("pipeline", pipelineDesignTab.serializePipeline());

        try {
            OutputStreamWriter out = new OutputStreamWriter(editedFile.write(false));
            try {
                renderer.writeJSONString(out);
                out.flush();
            } finally {
                out.close();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        pipelineDesignTab.setDirty(false);
    }

    private PopupMenu createTemplateMenu() {
        PopupMenu templateMenu = new PopupMenu();
        MenuItem menuItem = new MenuItem("Empty");
        menuItem.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        loadTemplate(Gdx.files.internal("template/empty-pipeline.json"));
                    }
                });
        templateMenu.addItem(menuItem);
        return templateMenu;
    }

    private void loadTemplate(FileHandle fileHandle) {
        if (pipelineDesignTab != null && pipelineDesignTab.isDirty()) {
            Dialogs.showErrorDialog(getStage(), "Current pipeline has been modified, close it or save it");
        } else {
            removeAllTabs();
            try {
                InputStream stream = fileHandle.read();
                try {
                    pipelineDesignTab = PipelineLoader.loadPipeline(stream, new UIPipelineLoaderCallback(skin));
                    pipelineDesignTab.awaken();
                    tabbedPane.add(pipelineDesignTab);
                    editedFile = null;
                } finally {
                    stream.close();
                }
            } catch (IOException exp) {
                throw new RuntimeException("Unable to load default pipeline definition", exp);
            }
        }
    }

    private void removeAllTabs() {
        tabbedPane.removeAll();
        insideTable.clearChildren();
    }

    public void resized(int width, int height) {
        for (Tab tab : tabbedPane.getTabs()) {
            ((AwareTab) tab).resized(width, height);
        }
    }

    public void dispose() {
        for (Tab tab : tabbedPane.getTabs()) {
            tab.dispose();
        }
    }
}
