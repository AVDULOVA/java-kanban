import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements NewTaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    HistoryManager historyManager;

    int taskId = 1;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }


    @Override
    public void addTask(Task task) {
        task.setId(taskId);
        taskId++;
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(taskId);
        taskId++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask, int epicId) {
        subTask.setId(taskId);
        taskId++;
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.getSubTasksId().add(subTask.getId());
            updateEpicStatus(epicId);
        }

    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();

    }

    @Override
    public void deleteSubTasks(int epicId) {
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            updateEpicStatus(epicId);
        }
        subTasks.remove(epicId);

    }

    @Override
    public void printTask() {
        for (Task task : tasks.values()) {
            System.out.println(task.toString());
        }
    }

    @Override
    public void printEpic() {
        for (Epic epic : epics.values()) {
            System.out.println(epic.toString());
        }

    }

    @Override
    public void printSubTask() {
        for (SubTask st : subTasks.values()) {
            System.out.println(st.toString());
        }

    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        ArrayList<Integer> subtaskIds = epics.get(id).getSubTasksId();
        for (Integer subtaskId : subtaskIds) {
            subTasks.remove(subtaskId);
        }
        epics.remove(id);


    }

    @Override
    public void deleteSubTask(int id, int epicId) {
        subTasks.remove(id);
        updateEpicStatus(epicId);
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }


    @Override
    public void updateTask(int newId, Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(int newId, Epic e) {
        epics.put(e.getId(), e);

    }

    @Override
    public void updateSubTask(int epicId, SubTask st) {
        subTasks.put(st.getId(), st);
        updateEpicStatus(epicId);

    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epic.getSubTasksId().isEmpty()) {
            ArrayList<Integer> subTasksId = epic.getSubTasksId();
            ArrayList<SubTask> subTasksTemp = new ArrayList<>();
            for (Integer st : subTasksId) {
                subTasksTemp.add(subTasks.get(st));
            }
            if (subTasksTemp.isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            } else {

                boolean checkStatusNew = true;
                boolean checkStatusDone = true;
                for (SubTask subTask : subTasksTemp) {
                    if (subTask != null && !(subTask.getStatus().equals(Status.NEW))) {
                        checkStatusNew = false;
                        break;
                    }
                }

                for (SubTask subTask : subTasksTemp) {
                    if (subTask != null && !(subTask.getStatus().equals(Status.DONE))) {
                        checkStatusDone = false;
                    }
                }
                if (checkStatusNew) {
                    epic.setStatus(Status.NEW);
                } else if (checkStatusDone) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTaskId : epic.getSubTasksId()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask != null) {
                subTasks.add(subTask);
                historyManager.add(subTask);
            }
        }
        return subTasks;
    }

    @Override
    public List<Task> getHistory() {
        if (historyManager.getHistory().size() > 10) {
            historyManager.getHistory().remove(0);
        }
        for (Task h : historyManager.getHistory()) {
            System.out.println(h);
        }
        return historyManager.getHistory();
    }
}
