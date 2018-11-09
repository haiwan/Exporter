package org.vaadin.haijian.helpers;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class PersonService {

	private static String[] firstName = { "James", "John", "Robert", "Michael", "William", "David", "Richard",
			"Charles", "Joseph", "Christopher", "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer",
			"Maria", "Susan", "Margaret", "Dorothy", "Lisa" };

	private static String[] lastName = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
			"Moore", "Taylor", "Andreson", "Thomas", "Jackson", "White" };

	private static List<Person> persons;
	public Stream<Person> getPersons(int offset, int limit, AgeGroup filter, List<QuerySortOrder> list) {
		ensureTestData();

		Stream<Person> filtered = persons.stream().filter(p -> filter(p, filter));

		// Sort by using a stream comparator
		if (list != null) {
			for (final QuerySortOrder o : list) {

				Comparator<Person> comp = null;
				switch (o.getSorted()) { // returns col id
					case "name":
						comp = Comparator.comparing(Person::getName);
						break;
					case "age":
						comp = Comparator.comparing(Person::getAge);
						break;
					case "birthday":
						comp = Comparator.comparing(Person::getBirthday);
						break;
					case "email":
						comp = Comparator.comparing(Person::getEmail);
						break;

					default:
						break;
				}

				if (o.getDirection() == SortDirection.DESCENDING) {
					comp = comp.reversed();
				}

				filtered = filtered.sorted(comp);
			}
		}

		return filtered.skip(offset).limit(limit);
	}

	public int totalSize(){
		ensureTestData();
		return persons.size();
	}

	public Collection<Person> findUsers(int start, int end){
		return persons.subList(start, end);
	}

	private void ensureTestData() {
		if (persons == null) {

			final Random r = new Random();

			persons = new ArrayList<>();
			for (int i = 0; i < 5000; i++) {
				final Person person = new Person();
				person.setName(firstName[r.nextInt(firstName.length)] + " " + lastName[r.nextInt(lastName.length)]);
				person.setAge(r.nextInt(50) + 18);
				person.setEmail(person.getName().replaceAll(" ", ".") + "@example.com");

				LocalDate date = LocalDate.now().minusYears(person.getAge());
				date = date.withMonth(r.nextInt(12) + 1);
				date = date.withDayOfMonth(r.nextInt(28) + 1);
				person.setBirthday(date);

				persons.add(person);
			}
		}
	}

	private boolean filter(Person p, AgeGroup filter) {
		if (filter == null) {
			return true;
		}

		final int age = p.getAge();
		return filter.getMinAge() <= age && filter.getMaxAge() >= age;
	}

	public int countPersons(int offset, int limit, AgeGroup filter) {
		ensureTestData();

		final long count = persons.stream().filter(p -> filter(p, filter)).skip(offset).limit(limit).count();

		return (int) count;
	}

}
