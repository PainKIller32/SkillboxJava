import entities.Department;
import entities.Employee;
import entities.Vocations;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Created by Danya on 26.10.2015.
 */
public class Loader {
    private static SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        setUp();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        //Ошибочные сотрудники
//        List<Employee> employees = (List<Employee>) session.createQuery("select d.headEmployee FROM Department d where d.id !=" +
//                " d.headEmployee.department.id").list();
//        System.out.println("Сотрудники ошибочно привязанные к отделу:");
//        for (Employee employee : employees) {
//            System.out.println(employee.getName());
//        }

        //Зарплата меньше 115000
//        List<Employee> employees = (List<Employee>) session.createQuery("select d.headEmployee FROM Department d where d.headEmployee.salary < 115000").list();
//        System.out.println("Зарплата руководителя меньше 115000:");
//        for (Employee employee : employees) {
//            System.out.println(employee.getName());
//        }

        //Вышли на работу до марта 2010
//        List<Employee> employees = (List<Employee>) session.createQuery("select d.headEmployee FROM Department d where d.headEmployee.hireDate < :date")
//                .setParameter("date", LocalDate.of(2010,3,1)).list();
//        System.out.println("Вышли на работу до марта 2010:");
//        for (Employee employee : employees) {
//            System.out.println(employee.getName());
//        }

//        List<Department> departments = (List<Department>) session.createQuery("FROM Department").list();
//        System.out.println("Список отделов производства:");
//        for (Department department : departments) {
//            System.out.println(department.getName());
//        }
//
//        Department dept = new Department("Отдел производства");
//        session.save(dept);
//        System.out.println("Сохранен отдел производства с id = " + dept.getId());

//        Department dept = (Department) session.createQuery("FROM Department WHERE name=:name")
//            .setParameter("name", "Отдел производства").list().get(0);
//        session.delete(dept);

        List<Vocations> vocations = (List<Vocations>) session.createQuery("FROM Vocations").list();
        if (vocations.isEmpty()) {
            List<Employee> employees = (List<Employee>) session.createQuery("FROM Employee").list();
            for (Employee employee : employees) {
                LocalDate start = employee.getHireDate();
                while (start.getYear() <= LocalDate.now().getYear() + 1) {
                    LocalDate startVocation = start.plusDays(rnd(false));
                    LocalDate endVocation = startVocation.plusDays(rnd(true));
                    Vocations voc = new Vocations(employee, startVocation, endVocation);
                    session.save(voc);
                    start = start.plusYears(1);
                }
            }
        }

        int j = 1;
        System.out.println("Пересечения отпусков сотрудников:");
        for (Vocations voc : vocations) {
            for (int i = j; i < vocations.size(); i++) {
                Vocations compared = vocations.get(i);
                if (!voc.getEmployee().equals(compared.getEmployee())) {
                    if (voc.getStartVocation().isAfter(compared.getStartVocation()) && voc.getStartVocation().isBefore(compared.getEndVocation())) {
                        if (voc.getEndVocation().isBefore(compared.getEndVocation())) {
                            System.out.println(voc.getEmployee().getName() + " и " + compared.getEmployee().getName());
                            System.out.println("c " + voc.getStartVocation() + " по " + voc.getEndVocation() + "\n");
                        } else {
                            System.out.println(voc.getEmployee().getName() + " и " + compared.getEmployee().getName());
                            System.out.println("c " + voc.getStartVocation() + " по " + compared.getEndVocation() + "\n");
                        }
                    } else {
                        if (voc.getEndVocation().isAfter(compared.getStartVocation()) && voc.getEndVocation().isBefore(compared.getEndVocation())) {
                            System.out.println(voc.getEmployee().getName() + " и " + compared.getEmployee().getName());
                            System.out.println("c " + compared.getStartVocation() + " по " + voc.getEndVocation() + "\n");
                        }
                    }
                    if (compared.getStartVocation().isAfter(voc.getStartVocation()) && compared.getEndVocation().isBefore(voc.getEndVocation())) {
                        System.out.println(voc.getEmployee().getName() + " и " + compared.getEmployee().getName());
                        System.out.println("c " + compared.getStartVocation() + " по " + compared.getEndVocation() + "\n");
                    }
                }
            }
            j++;
        }

        session.getTransaction().commit();
        session.close();

        //==================================================================
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    //=====================================================================

    private static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(new File("src/config/hibernate.cfg.xml")) // configures settings from hibernate.config.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    private static int rnd(Boolean end) {
        Random rand = new Random();
        if (end) {
            return rand.nextBoolean() ? 21 : 28;
        } else {
            return rand.nextInt(338);
        }
    }
}
