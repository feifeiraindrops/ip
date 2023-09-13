package benben;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The type Storage that stores, reads and rite the local file
 */
public class Storage {
    private final String filePath;

    private File file;

    /**
     * Instantiates a new Storage.
     *
     * @param filePath the file path of the local file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes the data from the task list to the local file
     *
     * @param tasks the task list
     */
    public void write(TaskList tasks) {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            String content = "";
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                content = content + t.getLog();
            }
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            throw new BenBenException("Failed to write to file!" + e.getMessage());
        }
    }

    /**
     * Load array list of tasks from the local file
     *
     * @return the array list of the tasks
     * @throws BenBenException the ben ben exception
     */
    public ArrayList<Task> load() throws BenBenException {
        ArrayList<Task> list = new ArrayList<>();
        try {
            this.file = new File(filePath);
            if (!file.exists()) {
                boolean isCreated = file.createNewFile();
                assert isCreated == true;
                return list;
            }
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                boolean canRead = false;
                String task = sc.nextLine();
                String[] strSplit = task.split("\\|");
                assert strSplit.length >= 3;
                for (int i = 0; i < strSplit.length; i++) {
                    strSplit[i] = strSplit[i].trim();
                }
                if (strSplit[0].startsWith("T") && strSplit.length == 3) {
                    Task nextTask = new Todo(strSplit[2]);
                    if (strSplit[1].startsWith("1")) {
                        nextTask.mark();
                        list.add(nextTask);
                        canRead = true;
                    } else if (strSplit[1].startsWith("0")) {
                        list.add(nextTask);
                        canRead = true;
                    }

                }
                if (strSplit[0].startsWith("D") && strSplit.length == 4) {
                    Task nextTask = new Deadline(strSplit[2], strSplit[3]);
                    if (strSplit[1].startsWith("1")) {
                        nextTask.mark();
                        list.add(nextTask);
                        canRead = true;
                    } else if (strSplit[1].startsWith("0")) {
                        list.add(nextTask);
                        canRead = true;
                    }
                }
                if (strSplit[0].startsWith("E") && strSplit.length == 5) {
                    Task nextTask = new Event(strSplit[2], strSplit[3], strSplit[4]);
                    if (strSplit[1].startsWith("1")) {
                        nextTask.mark();
                        list.add(nextTask);
                        canRead = true;
                    } else if (strSplit[1].startsWith("0")) {
                        list.add(nextTask);
                        canRead = true;
                    }

                }
                if (!canRead) {
                    throw new BenBenException("The file content is corrupted, please report this to admin");
                }
            }
            sc.close();
            return list;
        } catch (FileNotFoundException e) {
            throw new BenBenException("The local file is not found in the directory");
        } catch (IOException e) {
            throw new BenBenException("IOException found!" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BenBenException("The local file does not exits");
        }
    }
}
