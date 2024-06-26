import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId = new ArrayList<>();


    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.status = Status.NEW;
        this.subTasksId = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasksId.add(subTask.getId());
    }
    @Override
    public String toString() {
        return "Epics{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasksId=" + subTasksId +
                '}';
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }
}
