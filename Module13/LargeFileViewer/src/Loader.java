import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TreeMap;

public class Loader {
    private static File file;
    private static boolean edited = false;
    private static int fileIncrement = 0;
    private static long currentPosition = 0;
    private static long endChangedFragment = 0;
    private static long startChangedFragment = 0;
    private static int changedFragmentIncrement = 0;
    private static final long BUFFER_SIZE = 5_000;
    private static final long WRITE_BUFFER_SIZE = 1_000_000;
    private static JProgressBar progressBar = new JProgressBar();
    private static Charset actuallyCharset = StandardCharsets.UTF_8;
    private static ArrayList<Long> loadStrings = new ArrayList<>();
    private static ArrayList<Long> viewStrings = new ArrayList<>();
    private static ArrayList<Long> loadChanges = new ArrayList<>();
    private static StringBuilder viewFragment = new StringBuilder();
    private static TreeMap<Long, Changes> changeStrings = new TreeMap<>();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();

        Form form = new Form();
        frame.setContentPane(form.getRootPanel());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("FileViewer");

        frame.setSize(640, 480);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        form.onEdit((editPosition, increment) -> {
            if (!edited) {
                startChangedFragment = editPosition;
                endChangedFragment = editPosition;
                edited = true;
            } else {
                if (editPosition < startChangedFragment) {
                    startChangedFragment = editPosition;
                } else if (editPosition > endChangedFragment) {
                    endChangedFragment = editPosition;
                }
            }
            changedFragmentIncrement += increment;
        });

        form.onScroll((position, text) -> {
            if (edited) {
                saveChangeFragment(text);
                edited = false;
            }
            return nextViewFragment(position);
        });

        form.onOpen((fileOpen) -> file = fileOpen);
        form.onChangeCharset(Loader::setActuallyCharset);
        form.onSave(() -> {
            new FileSaver();
            return progressBar;
        });
    }

    private static void saveChangeFragment(Document text) {
        try {
            String changeFragment;
            long startSaveFragment = 0;
            long endSaveFragment = endChangedFragment + changedFragmentIncrement;
            for (long viewString : viewStrings) {
                if (viewString <= startChangedFragment) {
                    startSaveFragment = viewString;
                } else if (viewString >= endSaveFragment) {
                    endSaveFragment = viewString;
                    break;
                }
            }
            changeFragment = text.getText((int) startSaveFragment, (int) (endSaveFragment - changedFragmentIncrement - startSaveFragment));

            startSaveFragment = loadStrings.get(viewStrings.indexOf(startSaveFragment));
            endSaveFragment = loadStrings.get(viewStrings.indexOf(endSaveFragment));

            for (long loadChange : loadChanges) {
                if (loadChange >= startSaveFragment && loadChange < endSaveFragment) {
                    fileIncrement -= changeStrings.get(loadChange).getIncrement();
                    changeStrings.remove(loadChange);
                }
            }
            changeStrings.put(startSaveFragment, new Changes(startSaveFragment, endSaveFragment, changedFragmentIncrement, changeFragment));
            fileIncrement += changedFragmentIncrement;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        startChangedFragment = 0;
        endChangedFragment = 0;
        changedFragmentIncrement = 0;
    }

    private static StringBuilder nextViewFragment(long position) {
        if (position != 0) {
            setCurrentPosition(position);
        } else {
            currentPosition = position;
        }

        long end;
        if (currentPosition >= (file.length() - BUFFER_SIZE)) {
            end = file.length();
        } else {
            end = currentPosition + BUFFER_SIZE;
        }

        viewFragment = new StringBuilder();
        loadStrings = new ArrayList<>();
        viewStrings = new ArrayList<>();
        loadChanges = new ArrayList<>();
        if (changeStrings.isEmpty()) {
            return readFragmentToView(currentPosition, end);
        } else {
            long caretPosition = currentPosition;
            for (long change : changeStrings.keySet()) {
                if (change > end) {
                    break;
                }
                Changes currentChange = changeStrings.get(change);
                if (currentChange.getStart() < caretPosition && currentChange.getEnd() > caretPosition) {
                    viewFragment.append(readFragmentToView(caretPosition, currentChange.getEnd()));
                    caretPosition = currentChange.getEnd();
                } else if (change >= caretPosition && change < end) {
                    viewFragment.append(readFragmentToView(caretPosition, change));
                    viewFragment.append(currentChange.getText());
                    loadChanges.add(change);
                    end += currentChange.getIncrement();
                    if (currentChange.getEnd() < end) {
                        caretPosition = currentChange.getEnd();
                    } else {
                        return viewFragment;
                    }
                }
            }
            return viewFragment.append(readFragmentToView(caretPosition, end));
        }
    }

    private static void setCurrentPosition(long position) {
        try (RandomAccessFile reader = new RandomAccessFile(file, "r")) {
            reader.seek(position);
            currentPosition = position;

            for (Long change : changeStrings.keySet()) {
                if (change < currentPosition) {
                    Changes currentChange = changeStrings.get(change);
                    if (currentChange.getEnd() > currentPosition) {
                        currentPosition = currentChange.getEnd();
                    } else {
                        currentPosition += currentChange.getIncrement();
                    }
                } else {
                    break;
                }
            }
            reader.readLine();
            currentPosition = reader.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder readFragmentToView(long startPosition, long endPosition) {
        StringBuilder builder = new StringBuilder();
        try (RandomAccessFile reader = new RandomAccessFile(file, "r")) {
            reader.seek(startPosition);
            loadStrings.add(startPosition);
            while (reader.getFilePointer() < endPosition) {
                builder.append(new String(reader.readLine().getBytes(StandardCharsets.ISO_8859_1), actuallyCharset))
                        .append("\n");
                loadStrings.add(reader.getFilePointer());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewStrings.add((long) 0);
        for (int i = 0; i < builder.length(); i++) {
            if (builder.charAt(i) == '\n') {
                viewStrings.add((long) (i + 1 + viewFragment.length()));
            }
        }
        return builder;
    }

    private static class FileSaver extends Thread {

        public FileSaver() {
            this.start();
        }

        @Override
        public void run() {
            if (!changeStrings.isEmpty()) {
                String originalFileName = file.getAbsolutePath();
                File tempFile = new File(file.getParent() + "\\temp");
                long caretPosition = 0;
                try (FileOutputStream fw = new FileOutputStream(tempFile)) {
                    fw.getChannel().force(true);
                    for (long change : changeStrings.keySet()) {
                        Changes currentChange = changeStrings.get(change);
                        if ((change - caretPosition) > WRITE_BUFFER_SIZE) {
                            for (int i = (int) caretPosition; i < change - WRITE_BUFFER_SIZE; i += WRITE_BUFFER_SIZE) {
                                fw.write(readFragmentToSave(caretPosition, caretPosition + WRITE_BUFFER_SIZE).toString().getBytes());
                                caretPosition += WRITE_BUFFER_SIZE;
                            }
                        }
                        fw.write(readFragmentToSave(caretPosition, change).toString().getBytes());
                        fw.write(currentChange.getText().getBytes());
                        caretPosition = currentChange.getEnd();
                    }
                    for (int i = (int) caretPosition; i < file.length() - WRITE_BUFFER_SIZE; i += WRITE_BUFFER_SIZE) {
                        fw.write(readFragmentToSave(caretPosition, caretPosition + WRITE_BUFFER_SIZE).toString().getBytes());
                        progressBar.setMaximum((int) (file.length() + fileIncrement));
                        progressBar.setValue((int) (caretPosition));
                        caretPosition += WRITE_BUFFER_SIZE;
                    }
                    fw.write(readFragmentToSave(caretPosition, file.length()).toString().getBytes());
                    progressBar.setValue(100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.delete()) {
                    tempFile.renameTo(new File(originalFileName));
                }
                changeStrings = new TreeMap<>();
            }
        }
    }

    private static StringBuilder readFragmentToSave(long startPosition, long endPosition) {
        StringBuilder builder = new StringBuilder();
        byte[] fragment = new byte[(int) (endPosition - startPosition)];
        try (RandomAccessFile reader = new RandomAccessFile(file, "r")) {
            reader.seek(startPosition);
            while (reader.getFilePointer() < endPosition) {
                reader.read(fragment);
                builder.append(new String(fragment, actuallyCharset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    private static void setActuallyCharset(String charset) {
        actuallyCharset = Charset.forName(charset);
    }
}