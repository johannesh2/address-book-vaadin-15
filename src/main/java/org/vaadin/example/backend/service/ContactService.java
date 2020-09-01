package org.vaadin.example.backend.service;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.Company;
import org.vaadin.example.backend.entity.Contact;
import org.vaadin.example.backend.repository.CompanyRepository;
import org.vaadin.example.backend.repository.ContactRepository;

@Service
public class ContactService {
    private static final Logger LOGGER = Logger
            .getLogger(ContactService.class.getName());
    private ContactRepository contactRepository;
    private CompanyRepository companyRepository;

    public ContactService(ContactRepository contactRepository,
            CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public List<Contact> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(filterText);
        }
    }

    public List<Contact> findAll(int page, int pageSize) {
        return contactRepository.findAll(PageRequest.of(page, pageSize))
                .toList();
    }

    public long count() {
        return contactRepository.count();
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public void save(Contact contact) {
        if (contact == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }

        var dbContact = contact.getId() == null ? new Contact()
                : contactRepository.findById(contact.getId()).orElse(null);
        dbContact.setFirstName(contact.getFirstName());
        dbContact.setLastName(contact.getLastName());
        dbContact.setEmail(contact.getEmail());

        if (dbContact.getCompany() == null || !contact.getCompany().getId()
                .equals(dbContact.getCompany().getId())) {
            companyRepository.findById(contact.getCompany().getId())
                    .ifPresent(dbContact::setCompany);
        }

        contactRepository.save(contact);
    }

    @PostConstruct
    public void populateTestData() {
        if (companyRepository.count() == 0) {
            companyRepository.saveAll(Stream
                    .of("Path-Way Electronics", "E-Tech Management",
                            "Path-E-Tech Management")
                    .map(Company::new).collect(Collectors.toList()));
        }

        if (contactRepository.count() == 0) {
            Random r = new Random(0);
            List<Company> companies = companyRepository.findAll();
            contactRepository.saveAll(Stream.of(names).map(name -> {
                String[] split = name.split(",");
                Contact contact = new Contact();
                contact.setFirstName(split[0]);
                contact.setLastName(split[1]);
                contact.setCompany(companies.get(r.nextInt(companies.size())));
                String email = (contact.getFirstName() + "."
                        + contact.getLastName() + "@" + contact.getCompany()
                                .getName().replaceAll("[\\s-]", "")
                        + ".com").toLowerCase();
                contact.setEmail(email);
                return contact;
            }).collect(Collectors.toList()));
        }
    }

    private static final String[] names = new String[] { "Kirk,Mendoza",
            "Roy,Lawrence", "Willie,Harvey", "Alexa,Adams", "Carrie,Schmidt",
            "Bradley,Dean", "Jimmy,Torres", "Rosemary,Warren", "Christy,Graves",
            "Jim,Castro", "Allen,Williams", "Tomothy,Beck", "Sergio,Curtis",
            "Lena,Robertson", "Alexa,Caldwell", "Mia,Jones", "Max,Torres",
            "Charles,Douglas", "Roy,Thomas", "Patrick,Bates", "Earl,Long",
            "Savannah,Bennett", "Elmer,Peters", "Christina,Smith",
            "Christopher,Byrd", "Krin,Hicks", "Cindy,Herrera", "Tom,Moore",
            "Greg,Shelton", "Noah,Lewis", "Tracy,Stevens", "Tyrone,Wallace",
            "Everett,Roberts", "Brooklyn,Harrison", "Kristina,Wells",
            "Paula,Cooper", "Vera,Kelley", "Dawn,Mills", "Ken,Duncan",
            "Roy,Brooks", "Stephanie,Alexander", "Phillip,Garrett",
            "Daniel,Davis", "Milton,Herrera", "Austin,Harris", "Harold,Howell",
            "Kristen,Martinez", "Holly,Sutton", "Abigail,Barrett",
            "Darlene,Johnson", "Calvin,Mason", "Leta,Price", "Roland,Russell",
            "Elmer,Young", "Penny,Olson", "Frances,Adams", "Ian,Bennett",
            "Henry,Fox", "Wilma,Douglas", "Paul,Wade", "Melinda,Walters",
            "Claire,Hicks", "Marshall,Myers", "Shelly,Bowman", "Georgia,Castro",
            "Irene,Shaw", "Yolanda,Fleming", "Arron,Mckinney",
            "Eleanor,Gutierrez", "Sebastian,Collins", "Brad,Price",
            "Perry,Griffin", "Lillie,Ford", "Dan,Dixon", "Bradley,Edwards",
            "William,Long", "Marion,Nguyen", "Roger,Phillips", "Kenzi,Adams",
            "Marcus,Weaver", "Jo,Steeves", "Travis,Medina", "Lonnie,Banks",
            "Patsy,Riley", "Carmen,Day", "Lester,Adams", "Serenity,Parker",
            "Bernice,Martin", "Cathy,Coleman", "Arlene,Soto", "Jerry,Fletcher",
            "Joan,Fleming", "Terrence,Payne", "Beatrice,Jordan", "Anna,Moreno",
            "Alvin,Ruiz", "Bobbie,Craig", "Brianna,King", "Morris,Gilbert",
            "Julia,Torres", "Carolyn,Herrera", "Cecil,Roberts",
            "Alexis,Ramirez", "Stanley,Stewart", "Marion,Daniels",
            "Felecia,Sutton", "Marian,Byrd", "Nina,Jacobs", "Jesse,King",
            "Jeanne,White", "Mattie,Alexander", "Leslie,Hicks",
            "Courtney,Wallace", "Marie,Little", "Kevin,Harris", "Terry,Ruiz",
            "Alex,Knight", "Kristin,Hudson", "Amy,Scott", "Duane,Rodriquez",
            "Elmer,Vasquez", "Frances,Vasquez", "Jacob,Chambers",
            "Andre,Porter", "Melvin,Herrera", "Wallace,Hudson", "Diane,Frazier",
            "Leroy,Matthews", "Jared,Jackson", "Rafael,Jones", "Jayden,Warren",
            "Bella,Evans", "Gabe,Fisher", "Tracy,Jacobs", "Ryan,Long",
            "Daryl,Palmer", "Rosa,Kennedy", "Raul,Mckinney", "Violet,Jennings",
            "Tomothy,Rice", "Duane,Lambert", "Mary,Soto", "Miriam,Brooks",
            "Savannah,Franklin", "Brandy,Daniels", "Jack,Neal", "Emily,Berry",
            "Joel,Gonzalez", "Lucille,Horton", "Alfred,Lambert", "Ron,Newman",
            "Jar,Jennings", "Judy,Knight", "Andrew,Herrera", "Wayne,Reyes",
            "Bella,Grant", "Alan,Pena", "Shane,Curtis", "Penny,Cruz",
            "Gabriel,Morris", "Jessica,Stanley", "Lori,Simpson", "Kelly,Jones",
            "Ross,Johnston", "Dylan,Mcdonalid", "Darren,Bryant", "Bill,Hale",
            "Janice,Lucas", "Travis,Knight", "Derrick,Bradley", "Holly,Stewart",
            "Brayden,Owens", "Ella,Steward", "Kitty,Lambert", "Logan,Murray",
            "Debra,Stone", "Tomothy,Hansen", "Deanna,Shelton", "Tonya,Wade",
            "Nina,Wallace", "Debra,Wallace", "Mary,Payne", "Guy,Garrett",
            "Savannah,Cole", "Gwendolyn,Holt", "Marilyn,Burns",
            "Carole,Pearson", "Mia,Bowman", "Jar,Reynolds", "Howard,Gibson",
            "Franklin,Hansen", "Anne,Dean", "Frank,Bates", "Diane,Romero",
            "Carolyn,Fernandez", "Bertha,Adams", "Vera,Owens", "Cathy,Oliver",
            "Ritthy,Evans", "Rita,Jones", "Rita,Willis", "Daniel,Warren",
            "Jeffery,Fernandez", "Ellen,Rice", "Derek,Gibson", "Charlie,Howell",
            "Neil,Butler", "Mia,Lewis", "Terrance,Nguyen", "Neil,Ramirez",
            "Sherry,Sutton", "Taylor,Burton", "Joyce,Richardson",
            "Kevin,Warren", "Carrie,Barrett", "Kristina,Oliver",
            "Brandy,Romero", "Cassandra,Griffin", "Jeanne,Mendoza",
            "Jane,Williams", "Scarlett,Fleming", "Erika,Howell",
            "Franklin,Bailey", "Ricky,Ortiz", "Amanda,Ramirez",
            "Claudia,Fields", "Ruben,Hernandez", "Loretta,Long", "Martha,Hall",
            "Mildred,Henry", "Stacy,Jackson", "Pedro,Garza", "Jeremy,Stanley",
            "Elijah,Mendoza", "Chris,Hernandez", "Ritthy,Pierce",
            "Jeffery,Shaw", "Hunter,Taylor", "Louise,Harrison",
            "Carmen,Anderson", "Michelle,Wright", "Kelly,Woods", "Eric,Reyes",
            "Harold,Kennedy", "Evan,Carter", "Earl,Palmer", "Connor,Shaw",
            "Gabe,Olson", "Jeremy,Sims", "Dwight,Mills", "Mildred,Reyes",
            "Camila,Diaz", "Andy,Castillo", "Regina,Alvarez", "Nora,Morrison",
            "Floyd,Carter", "Brett,Nichols", "Josephine,Barrett", "Tina,Terry",
            "Marshall,Stone", "Chester,Morrison", "Mabel,Fisher",
            "Philip,Mccoy", "Paula,George", "Peyton,Jackson", "Lillie,Gregory",
            "Amber,Crawford", "Caleb,Wells", "Emma,Bell", "Ruben,Rogers",
            "Florence,Ruiz", "Warren,Hansen", "Richard,Rodriquez",
            "Brandon,Clark", "Don,Gordon", "Joshua,Hall", "Tyler,Rose",
            "Leon,Schmidt", "Kevin,Hicks", "Mae,Stephens", "Beth,Gibson",
            "Tammy,Matthews", "Christopher,Schmidt", "Mario,Butler",
            "Jorge,Palmer", "Teresa,Allen", "Darryl,Larson",
            "Carrie,Richardson", "Gregory,Bates", "Lorraine,Robertson",
            "Stephanie,Miles", "Jackson,Gibson", "Debra,Austin",
            "Terrance,Gonzales", "Hazel,Jensen", "Albert,Rice", "Terra,Freeman",
            "Russell,Bates", "Mary,Cruz", "Kelly,Johnson", "Oscar,Sanders",
            "Sally,Brooks", "Terrance,Kennedy", "Troy,Webb", "Harry,Long",
            "Derrick,Barrett", "Lois,Horton", "Darryl,Pierce", "Derrick,Hudson",
            "Robert,Cole", "Alma,Black", "Shawn,Beck", "Pamela,Nelson",
            "Terri,Bryant", "Penny,Daniels", "Vickie,Morales", "Dwayne,Barnett",
            "Daisy,Lee", "Nicholas,Olson", "Brittany,Hamilton", "Dave,Ward",
            "Bobby,Graves", "Dan,Morris", "Kylie,Mendoza", "Hailey,George",
            "Ian,Mccoy", "Harry,Hudson", "Kathryn,Johnston", "Dylan,Allen",
            "Tomothy,Pierce", "Cecil,Thompson", "Antonio,Mason", "Aiden,Powell",
            "Katrina,Reid", "Billie,Willis", "Brad,Garza", "Grace,Marshall",
            "Kitty,Romero", "Stacy,Nelson", "Kirk,Herrera", "Paul,Powell",
            "Gabriel,Reynolds", "Megan,Hansen", "Caroline,Lawson",
            "Martha,Pena", "Levi,Meyer", "John,Sanders", "Rosa,Hall",
            "Jennifer,Newman", "Brian,Torres", "Sara,Gordon", "Derrick,Snyder",
            "Judith,Martin", "Katrina,White", "Norman,Rodriguez",
            "Isaac,Torres", "Isaac,Grant", "Diana,Silva", "Bill,Jennings",
            "Elijah,Sims", "Veronica,Carter", "Christian,Ford", "Chris,Black",
            "Dianne,Oliver", "Madison,Alexander", "Daryl,Hunter",
            "Connie,Mcdonalid", "Leta,Lawson", "Jason,James", "Sophie,Campbell",
            "Marie,Gutierrez", "Jayden,Phillips", "Olivia,Jimenez",
            "Brooklyn,Fisher", "Diane,King", "Vanessa,Washington",
            "Crystal,Larson", "Nathaniel,Perez", "Reginald,Walters",
            "Samuel,Mendoza", "Stella,Obrien", "Jose,Cole", "James,Clark",
            "Wilma,Warren", "Ted,Webb", "Ross,Hayes", "Sean,Peck",
            "Gabriel,Simmmons", "Evelyn,Grant", "Tomothy,Schmidt", "Vera,White",
            "Courtney,Hunter", "Debra,Bates", "Rosemary,Baker", "Sofia,Butler",
            "Debbie,Garza", "Ted,Gray", "Tiffany,Wright", "Victoria,Shelton",
            "Isabella,Simmons", "Keith,Powell", "Ella,Warren", "Gail,Tucker",
            "Lillian,Pearson", "Erica,Hunt", "Steve,Mills", "Kim,Peterson",
            "Marlene,Hanson", "Jon,Peck", "Ryan,Barnett", "Sebastian,Torres",
            "Dolores,Stevens", "Erik,Fuller", "Katrina,Freeman",
            "Dianne,Peters", "Celina,West", "Bertha,Simmons", "Jim,Mitchell",
            "Eddie,Cox", "Liam,Douglas", "Max,May", "Anita,Nguyen",
            "David,Matthews", "Violet,Henry", "Jar,Hanson", "Sofia,Stewart",
            "Chloe,Rodriquez", "Mattie,Prescott", "Doris,Diaz",
            "Jackie,Spencer", "Rosemary,Silva", "Diane,Sanders", "Leonard,Carr",
            "Alice,Richards", "Antonio,Rodriguez", "Jane,Perez",
            "Clarence,Hale", "Vickie,Gonzales", "Aaron,Curtis",
            "Tiffany,Bennett", "Nathan,Edwards", "Ronnie,Simmons",
            "Jeremiah,Howard", "Micheal,Gardner", "Jose,Ross", "Violet,Peters",
            "Sally,Cook", "Nicole,Lynch", "Gary,Jordan", "Johnny,Peterson",
            "Maureen,Murray", "Monica,Medina", "Clara,Cole", "Rebecca,Gibson",
            "Veronica,Hicks", "Edwin,Armstrong", "Charlie,Moore",
            "Tiffany,Mcdonalid", "Rhonda,Foster", "Roberto,Powell",
            "Lester,Cole", "Ernest,Hill", "Roberta,Rose", "Sophie,Herrera",
            "Avery,Flores", "Audrey,West", "Andrew,Ford", "Kitty,Rivera",
            "Shawn,Brooks", "Bernard,Hayes", "Minnie,Gomez", "Gertrude,Turner",
            "Josephine,Anderson", "Melanie,Horton", "Virgil,Miller",
            "Salvador,Gomez", "Joshua,Riley", "Penny,Barrett", "Peter,Hughes",
            "Marian,Martin", "Kathy,Marshall", "Herbert,Ross", "Anita,Ford",
            "Edgar,Wood", "Allan,Brooks", "Vickie,Black", "Amy,Palmer",
            "Alfredo,Cox", "Rita,Watts", "Rosemary,Price", "Roberta,Warren",
            "Floyd,Wade", "Bessie,Sullivan", "Ken,Webb", "Eileen,Gonzalez",
            "Terrance,Jordan", "Gail,West", "Bob,Martinez", "Brennan,Baker",
            "Howard,Mendoza", "Dianne,Lynch", "Kent,Carroll", "Eileen,Ross",
            "Flenn,Hunter", "Vivan,Elliott", "Esther,Morales", "Gene,Castro",
            "Genesis,Bell", "Rachel,Ryan", "Elsie,Romero", "Ricky,Allen",
            "Lester,Byrd", "Jo,Graham", "Myrtle,Prescott", "Tammy,Watkins",
            "Kurt,Jacobs", "Elaine,Hansen", "Robert,Lynch", "Lonnie,Day",
            "Wendy,Porter", "Ethel,Neal", "Ava,James", "Jeremy,Chambers",
            "Cindy,Burton", "Bernice,Diaz", "Virgil,Berry", "Bonnie,Rice",
            "Peyton,Fletcher", "Jeanne,Cox", "Dolores,Welch", "Vicki,Kennedy",
            "Tracy,Woods", "Tamara,Young", "Kaylee,Silva", "Jason,Webb",
            "April,Steeves", "Kathy,Robertson", "Melinda,Powell",
            "Tyrone,Chavez", "Jimmie,Herrera", "Marion,Carpenter", "Mark,Kuhn",
            "Georgia,Garcia", "Marjorie,Steward", "Tracey,Nelson",
            "Hilda,Terry", "Aubrey,Sims", "Jeff,Parker", "Howard,Owens",
            "Erik,Hale", "Daniel,Lynch", "Nathaniel,Holland", "Leta,Sanchez",
            "Robert,Caldwell", "Brennan,Peters", "Bernice,Vargas",
            "Felicia,Lawson", "Herbert,George", "Emily,Stewart", "Mabel,Jordan",
            "Dennis,Steward", "Layla,Watkins", "Alberto,Douglas",
            "Joanne,Powell", "Rebecca,Murphy", "Crystal,Wagner", "Carl,Vargas",
            "Troy,Burns", "Gregory,Armstrong", "Micheal,Harper",
            "Warren,Parker", "Aiden,Shaw", "Micheal,Herrera", "Brianna,Walters",
            "Yvonne,Wheeler", "Cody,Russell", "Cindy,Mcdonalid",
            "Francisco,Wagner", "Genesis,Graves", "Louis,Mason",
            "Marion,Pierce", "Leslie,Scott", "Joel,Taylor", "Randy,Armstrong",
            "Naomi,Vargas", "Greg,Vasquez", "Jacqueline,Carter",
            "Everett,Adams", "Warren,Barrett", "Anne,Howell", "Gerald,Wilson",
            "Louis,Griffin", "Oscar,Obrien", "Richard,Pearson", "Julia,Steeves",
            "Billie,Campbell", "Renee,Phillips", "Floyd,Carroll", "Jack,Day",
            "Ann,Lewis", "Denise,Pierce", "Lena,Andrews", "Alice,Peters",
            "Sergio,Miller", "Wendy,Walters", "Derrick,Hopkins", "Beth,Green",
            "Andre,Jensen", "Margie,Nichols", "Javier,Knight", "Harper,Kelley",
            "Troy,Wagner", "Alma,Ryan", "Ronald,Ryan", "Ross,Garrett",
            "Elizabeth,Griffin", "Corey,Vargas", "Perry,Steeves",
            "Joel,Stewart", "Genesis,Walker", "Bruce,Holland", "Marion,Black",
            "Julian,Rogers", "Tracy,Harvey", "Rachel,Hayes", "Dean,Bell",
            "Julie,Olson", "Walter,Edwards", "Joe,Price", "Dustin,Coleman",
            "Isobel,Campbell", "Hailey,Burns", "Tonya,Ruiz", "Jon,Bowman",
            "Caleb,Ferguson", "Glenda,Jacobs", "Kaylee,Johnston",
            "Brennan,Cook", "Terri,Myers", "Sally,Morgan", "Jack,Nelson",
            "Vicki,Carlson", "Pauline,Pearson", "Troy,Simmmons", "Ana,Perez",
            "Amy,Price", "Holly,Evans", "Jeff,Rogers", "Tiffany,Ferguson",
            "Kathryn,Byrd", "Clayton,Lee", "Monica,Schmidt", "Deanna,Webb",
            "Lena,Pierce", "Lucas,Bradley", "Roberta,Peterson", "Glen,Mason",
            "Amelia,Davis", "Norman,Steeves", "Peggy,Carr", "Lois,Castro",
            "Charlene,Kuhn", "Kathryn,Payne", "Clifford,Burton",
            "Priscilla,Montgomery", "Gene,Bennett", "Theresa,Rodriguez",
            "Rita,Tucker", "Andrea,Hernandez", "Evelyn,Chapman",
            "Irma,Peterson", "Melvin,Ford", "Lori,May", "Tom,Knight",
            "Edward,Peck", "Rene,Butler", "Brayden,Cooper", "Alex,Lewis",
            "Allen,Rodriguez", "Kelly,Holmes", "Noah,Oliver", "Kathy,Mills",
            "Brayden,Rice", "Jeffery,Harrison", "Jerome,Steward",
            "Sherry,Coleman", "Connie,Watts", "Cory,James", "Alfredo,Flores",
            "Leonard,Morales", "Jeremiah,Russell", "Michael,Wallace",
            "Randy,Sims", "Ben,Willis", "Cathy,Jackson", "Lloyd,Austin",
            "Lucy,Stewart", "Gregory,Smith", "Norma,Wade", "Raul,Perez",
            "Martha,Gonzalez", "Jane,Oliver", "Candice,Hoffman",
            "Evelyn,Herrera", "Gary,Gonzalez", "Francis,Wallace",
            "Wanda,Barnes", "Francisco,Moore", "Elaine,Richardson",
            "Seth,Ramirez", "Lynn,Hale", "Tara,Lawrence", "Erica,Herrera",
            "Willie,Craig", "Lisa,Fuller", "Charlotte,Lucas", "Edna,Moreno",
            "Jose,Duncan", "Sarah,Woods", "Myrtle,Wade", "Jean,Stevens",
            "Danielle,Garza", "Lee,Andrews", "Darren,Moreno", "Larry,Schmidt",
            "Ritthy,Cooper", "Floyd,Long", "Jon,Peterson", "Herminia,James",
            "Darlene,Wade", "Avery,Hart", "Ruby,Kuhn", "Andy,Mason",
            "Caleb,Green", "Sharlene,Ortiz", "Mario,Long", "Leonard,Taylor",
            "Rick,Gomez", "Edgar,Weaver", "Brayden,Rivera", "Gladys,Wilson",
            "Mike,Fletcher", "Pamela,Boyd", "Alma,Miles", "Leroy,Austin",
            "Bill,Young", "Kim,Garcia", "Elmer,Butler", "Amy,Gonzales",
            "Jesus,Jensen", "Candice,Byrd", "Lydia,Smith", "Isabella,Watkins",
            "Tim,Weaver", "Manuel,Nguyen", "Martha,Day", "Edith,Miller",
            "Carolyn,Arnold", "Erika,Hoffman", "Marvin,Perkins", "Seth,Duncan",
            "Zoe,Bell", "Joan,Sullivan", "Courtney,Holmes", "Glenda,Beck",
            "Carole,Gray", "Nelson,Castillo", "Tim,Williamson", "Randall,Hart",
            "Matthew,Roberts", "Stella,Obrien", "Travis,Hicks", "Carole,Ruiz",
            "Genesis,Castro", "Tracy,Soto", "Gene,Castro", "Carole,Jennings",
            "Genesis,Hale", "Ross,Stewart", "Miriam,Oliver", "Patrick,Stone",
            "Nevaeh,Rogers", "Harry,Garcia", "Troy,Soto", "Heather,Butler",
            "Willard,Kuhn", "Joe,Holmes", "Dave,Campbell", "Hector,Romero",
            "Philip,Rivera", "Marian,Powell", "Leslie,Mason", "Martha,Henry",
            "Terri,Woods", "Hilda,Ray", "Sofia,Ryan", "Erica,Obrien",
            "Marshall,Curtis", "Marcus,Chambers", "Ruben,Cooper",
            "Dianne,Scott", "Julia,Reyes", "Pedro,Fuller", "Minnie,Edwards",
            "Hannah,Burton", "Darlene,Garrett", "Lisa,Beck", "Bessie,Tucker",
            "Hector,Perry", "Leona,Fowler", "Louise,Green", "Gladys,Perry",
            "Glen,Miller", "Lisa,Little", "Victoria,Hill", "Arnold,Stevens",
            "Diana,Rivera", "Kenzi,Terry", "Jackie,Hart", "Willard,Jordan",
            "Carter,Franklin", "Brooklyn,Evans", "Lucy,Cole", "Rose,Fernandez",
            "Evan,Wood", "Ian,Miles", "Mary,Wallace", "Elijah,Allen",
            "Isaac,George", "Levi,Robinson", "Bertha,Neal", "Javier,Graves",
            "Robert,Davidson", "Edna,Gomez", "Monica,Hopkins",
            "Beatrice,Austin", "Teresa,Peters", "Leslie,Stone", "Brian,May",
            "Carl,Bailey", "Walter,Scott", "Raymond,Franklin", "Rose,Dean",
            "Micheal,Meyer", "Marian,Cooper", "Lena,Curtis", "Virgil,Robertson",
            "Pamela,Miller", "Jeffery,Reid", "Ana,Graves", "Violet,Lawson",
            "Juan,Mills", "Priscilla,Knight", "Ryan,Banks", "Melissa,Simpson",
            "Taylor,Soto", "Francisco,Bates", "Erika,Spencer", "Taylor,Wells",
            "Raymond,Rhodes", "Taylor,Morgan", "Jessie,Lane", "Jennie,Stewart",
            "Adam,Armstrong", "Kristin,Fields", "Terri,Ward", "Jenny,Peck",
            "Alberto,Powell", "Stephanie,Bell", "Russell,Lewis",
            "Brennan,Cunningham", "Daisy,Mendoza", "Elaine,Robinson",
            "Willie,Castillo", "Dwayne,Hunter", "Vanessa,Hill", "Duane,Ramirez",
            "Willie,Fisher", "Savannah,Walters", "Darlene,Black", "Janet,Olson",
            "Daryl,Palmer", "Ben,Jacobs", "Katie,Kelley", "Lydia,Thompson",
            "Hailey,Morrison", "Rafael,Rodriquez", "Kathy,Mitchell",
            "Oscar,Kelly", "Becky,Ellis", "Brandy,Torres", "Rafael,Peters",
            "Connie,Shaw", "Elsie,Andrews", "Lillian,Harper",
            "Phillip,Johnston", "Marc,Matthews", "Miguel,Welch", "Paula,Lawson",
            "Aaron,Jacobs", "Michele,Rodriquez", "Abigail,Williamson",
            "Alma,Miller", "Everett,Alvarez", "Theresa,Elliott", "Tracy,Baker",
            "Jamie,Hale", "Leslie,Stephens", "Patsy,Elliott", "Annie,Gregory",
            "Caleb,Wallace", "Tracey,Lawrence", "Leon,Fowler", "Margie,Dunn",
            "Andy,Pierce", "Diane,Vasquez", "Eugene,Newman", "Adrian,Andrews",
            "Jim,Martinez", "Alan,Phillips", "Sandra,Davis", "Amber,Newman",
            "Danielle,Martinez", "Angel,Nguyen", "Judd,Weaver",
            "Paula,Fletcher", "Amelia,Scott", "Steven,Bennett", "Isaac,Wade",
            "Ross,Campbell", "Karl,Fleming", "Brandy,Warren", "Ricardo,Cole",
            "Jeffery,Cook", "Kelly,Wade", "Marcus,Lopez", "Jo,Shelton",
            "Lance,Myers", "Alan,Roberts", "Ethan,Walker", "Tracy,Johnston",
            "Jacqueline,Sutton", "Hunter,Sims", "Noelle,Steeves",
            "Jill,Simmmons", "Miriam,Romero", "Darryl,Burke", "Zoey,Ross",
            "Becky,Oliver", "Edgar,Henry", "Christina,Alexander",
            "Tracy,Hamilton", "Tammy,Simmons", "Aubree,Pena",
            "Brayden,Franklin", "Jesus,Barnes", "Logan,Romero", "Martha,Hansen",
            "Tanya,Sullivan", "Sonia,Hill", "Violet,Vasquez", "Crystal,Edwards",
            "Tina,Nguyen", "Enrique,Neal", "Jared,Hanson", "Logan,Walker",
            "Owen,Beck", "Randy,Grant", "Jerry,Murphy", "Leo,Lewis",
            "Kirk,Washington", "Maureen,Chapman", "Antonio,Carr",
            "Michelle,Hansen", "Jonathan,Sanchez", "Wayne,Bates",
            "Jesus,Campbell", "Travis,Graves", "Shannon,Daniels",
            "Hannah,Arnold", "Layla,Murray", "Bob,Robinson", "Billie,Richards",
            "Alfred,Schmidt", "Timmothy,Lopez", "Violet,Bryant",
            "Felecia,Carpenter", "Andrew,Davis", "Soham,Davis", "Janice,Woods",
            "Terry,Jenkins", "Tristan,Beck", "Grace,Morrison", "Lily,Oliver",
            "Marion,Craig", "Kelly,Reid", "Lena,Ryan", "Landon,Martin",
            "Ronald,Wood", "Ronnie,Robinson" };
}
