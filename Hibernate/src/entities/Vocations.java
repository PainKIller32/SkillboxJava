package entities;

import java.time.LocalDate;

public class Vocations {
    private Integer id;
    private Employee employee;
    private LocalDate startVocation;
    private LocalDate endVocation;

    public Vocations() {
        //Used by Hibernate
    }

    public Vocations(Employee employee, LocalDate startVocation, LocalDate endVocation){
        this.employee = employee;
        this.startVocation = startVocation;
        this.endVocation = endVocation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getStartVocation() {
        return startVocation;
    }

    public void setStartVocation(LocalDate startVocation) {
        this.startVocation = startVocation;
    }

    public LocalDate getEndVocation() {
        return endVocation;
    }

    public void setEndVocation(LocalDate endVocation) {
        this.endVocation = endVocation;
    }
}
