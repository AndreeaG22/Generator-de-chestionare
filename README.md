# _TEMA1_ -> Generator de chestionare
### Matei Andreea-Gabriela 323CB

## Descriere
Tema consta in implementarea unui generator simplu de chestionare. In cadrul programului, utilizatorul poate crea intrebari, crea chestionare si raspunde la chestionare.

## Implementare
### Clase utilizate si rolul lor general
`TestValidity` -> clasa care contine metode statice ce valideaza inputul utilizatorului si care, in cazul unui input valid, se foloseste de clasele si metodele corespunzatoare pentru a crea obiecte si pentru a executa instructiunile cerute de utilizator. Acesta mosteneste clasa 'UsersToAdd' pentru a ne fi mai usor sa verificam daca utilizatorul a introdus corect credentialele de la tastatura, verificare folosita la majoritatea apelurilor de sistem.

`UserToAdd` -> clasa care modeleaza datele introduse despre un utilizator ce trebuie adaugat. Metodele clasei verifica daca datele de input legate de user si parola sunt introduse corect. In caz afirmativ, datele despre noul utilizator sunt introduse in fisier.

`Users`-> clasa care contine informatiile despre un utilizator si care, cu ajutorul metodelor, prelucreaza datele din fisierul cu utilizatori.

`QuestionToAdd` ->clasa cu ajutorul careia modelam datele introduse despre o intrebare ce trebuie adaugata. Metodele clasei verifica daca datele de input legate de intrebare si raspunsuri sunt introduse corect. In caz afirmativ, datele despre noua intrebare sunt introduse in fisier.

`Question` -> clasa care contine informatiile despre o intrebare si care prelucreaza datele din fisiere cu ajutorul metodelor implementate.

`QuizzToAdd` -> clasa cu ajutorul careia modelam datele introduse despre un chestionar ce trebuie adaugat. Metodele clasei verifica daca datele de input legate de chestionar si intrebari sunt introduse corect. Tododata, una dintre metodele clasei ne ajuta la calcularea punctajului obtinut de un utilizator, in urma raspunsurilor acestuia.

`Quizz` -> clasa care contine informatiile despre un chestionar si care prelucreaza datele din fisiere cu ajutorul metodelor implementate.

`Answer` -> clasa care contine informatiile despre un raspuns si metode care intnorc informatii legate de rapsunsurile stocate in fisisere.

 `Tema1`-> clasa principala a programului, care contine metoda main si care apeleaza metodele din clasa 'TestValidity' pentru a executa instructiunile cerute de utilizator.

### Detaliere implementare pentru fiecare apel de sistem
#### 1. Crearea unui utilizator
##### Apel de sistem
```
–create-user -u ‘my_username’ -p ‘my_password’
```
<ul>
<li>Se apeleaza metoda 'verifyUserFormat' din clasa TestValidity.</li>
<li>Se apeleaza metoda checkCredentials din clasa 'UderToAdd'. Aceasta metoda verifica:</li>
->daca vectorul de argumente are lungimea mai mica decat 2 (adica apare doar comanda de creare a unui utilizator, fara username si parola) sau al doilea argument este egal cu '-u'(adica numele utilizatorului lipseste), atunci se afiseaza mesaj de eroare;

->daca vectorul de argumente are lungimea mai mica decat 3(adica apare doar comanda de creare utilizator si si username-ul) sau al doilea argument este egal cu '-p'(adica parola lipseste), atunci se afiseaza mesaj de eroare;
<li>se apeleaza metoda verificationUser din clasa UserToAdd, care intoarce 'true' daca in fisierul in care retinem utilizatorii apare inca un user cu acelasi nume cu cel introdus in linia de comanda. Daca exita un asemena user, atunci se afiseaza mesaj de eroare.</li>
<li>daca in urma verificarilor nu a rezultat nicio eroare, atunci se creeaza un nou utilizator(un obiect de tip User) si se apeleaza functia 'writeUserInFile', care scrie username-ul si parola in fisier. De asemenea, toti utilizatorii adaugati sunt retinuti intr-o lista. </li>
</ul>

#### 2. Crearea unei intrebari
##### Apel de sistem
```
–create-question -u ‘my_username’ -p ‘my_password’ -text ‘Cerul este albastru’ -type ‘single’ -answer-1 ‘Yes’ -answer-1-is-correct 1 -answer-2 ‘No’ -answer-2-is-correct 0 … -answer-i ‘Nothing’ -answer-i-is-correct 0
```
<ul>
<li>Se apeleaza metoda 'verifyQuestionFormat' din clasa TestValidity. Metoda apeleaza o alta metoda, checkAuthentication din clasa UserToAdd care, pe langa verificarile credentialelor, realizate la pasul anterior, acesata verifica daca username-ul dat ca argument exista in fisier. In caz contrat logarea nu a reusit.</li>
<li>Se apeleaza metoda 'verifyFormat' din clasa QuestionToAdd. Aceasta metoda verifica:</li>
->daca vectorul de argumente are lungimea mai mica decat 4(adica textul intrebarii nu apare) sau daca al patrulea argument este egal cu '-text'(adica textul intrebarii lipseste) sau daca al patrulea argument este egal cu '-type', atunci se afiseaza mesaj de eroare;

->daca intrebarea cu textul introdus exista deja in fisier, atunci se afiseaza mesaj de eroare;

->daca lungimea vectorului de argumente este mai mica decat 6 sau, daca in urma numararii raspunsurilor rezultatul este 0, atunci nu au fost introduse raspunsuri si se afiseaza mesaj de eroare(numarul raspunsurilor se calculeaza numarand argumentele din input care incep cu 'answer' si se termina cu 'correct');

->daca toate intrebarea are parametru de adevar si descriere, dar numarul de raspunsuri este egal cu 1, atunci se afiseaza mesaj de eroaare;

->daca numarul de raspunsuri este mai mare decat 5, sa va afisa mesaj de eroare;

->daca tipul intrebarii este 'single' si in urma numararii raspunsurilor cu parametru de adevar 1, rezultatul este mai mare decat 1, atunci se afiseaza mesaj de eroare;

->daca metoda 'verifyIfSameAnswer', care retine intr-un vector de string-uri toate raspunsurile introduse, gaseste 2 raspunsuri identice si intoarce 'true', atunci se afiseaza mesaj de eroare;

->daca nu exista descriere pentru un raspuns si numarul de raspunsuri este mai mic decat numarul de parametri de adevar, atunci se afiseaza mesaj de eroare, altfel, daca nu exista parametru de adevar pt o intrebare se afiseaza mesaj de eroare;

->altfel, se verifica indexul pe care trebuie sa il aiba intrebarea, la scrierea acesteia in fisier, in functie de numele utilizatorului care a creat intrebarea. Apoi, se creeaza un obiect de tip intrebare, toate raspunsurile se pun intr-o lista de raspunsuri, pentru ca mai apoi informatile despre intrebarea adaugata sa fie scrise in fisier. </li>
<li>forma pe care o vor avea informatiile unei intrebari in fisier este:</li>

`-question-info,username,question_text,question_id,question_tipe,number_of_correct_answers,total_number_of_answers`

`id_answer1/answe1/is_correct1,id_answer2/answer2/is_correct2,...id_answeri/answeri/is_correcti`
</ul>

#### 3. Afisarea ID-ului unei intrebari in functie de nume
##### Apel de sistem
`````
–get-question-id-by-text -u ‘my_username’ -p ‘my_password’ -text ‘Cerul este albastru’
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se apeleaza metoda 'findQuestionInFile', care, in functie de numele intrebarii intoarce din fisier id-ul ei sau null, daca intrebarea nu este gasita.</li>
</ul>

#### 4. Afisarea tuturor intrebarilor din sistem
##### Apel de sistem
`````
–get-all-questions -u ‘my_username’ -p ‘my_password’
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se creeaza un obiect de tip intrebare si se apeleaza metoda 'getAllQ' care intoarce un string ca rezultat, care contine textul si id-ul intrebarilor si care va fi afisat.</li>
</ul>

#### 5. Crearea unui chestionar
##### Apel de sistem
`````
–create-test -u ‘my_username’ -p ‘my_password’ -name ‘Chestionarul meu’ -question-1-id 1 -question-2-id 2 … -question-i-id i
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se apeleaza metoda 'checkQuizzFormat' care:</li>
->verifica daca exista, in fisierul de quiz-uri, un quiz cu acelasi nume si, in caz afirmativ, afiseaza mesaj de eroare;

->parcurge vectorul de argumente si verifica daca la exista un ID pentru fiecare intrebare; in caz contrar se va afisa mesaj de eroare

->numara intrebarile, iar daca sunt mai mult de 10, se va afisa mesaj de eroare;

<li>se stabileste indexul intrebarii in functie de numele utilizatorului</li>

<li>se apeleaza metoda 'implemetQuizz', care la randul ei apeleaza alte metode care adauga intr-o lista id-urile tuturor intrebarilor din quiz si adauga detaliile despre quiz, inclusiv lista de id-uri, in fiisier.</li>

<li>in fisier, un chestionar este retinut sub forma:</li>

``username,quiz_name,quiz_id,id_question1/id_question2/id_question3/....id_question10,is_completed``

</ul>


#### 6. Afisarea ID-ului unui chestionar in functie de nume
##### Apel de sistem
`````
–get-quiz-by-name -u ‘my_username’ -p ‘my_password’ -name ‘Chestionarul 1’
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se apeleaza functia 'verificationQuizz' care, in functie de numele chestionarului, intoarce fie id-ul acestuia, daca acesta este gasit in fisier fie null, in caz contrar.</li>
</ul>

#### 7. Afisarea tuturor chestionarelor din sistem
##### Apel de sistem
`````
–get-all-quizzes -u ‘my_username’ -p ‘my_password’
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se apeleaza metoda 'getAllQuizz' care intoarce un string cu toate informatiile necesare despre fiecare quiz(id-ul, numele, starea de completare a acestuia) si va afisa string-ul </li>
</ul>

#### 8. Afisarea detaliilor unui chestionar in functie de ID-ul acestuia
##### Apel de sistem
`````
–get-quiz-details-by-id -u ‘my_username’ -p ‘my_password’ -id 1
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se apeleaza metoda 'getQuizzDetails' care, in functie de id-ul quiz-ului, intoarce un string cu toate informatiile despre quiz si va afisa string-ul</li>
<li>pentru construirea string-ului care fa fi afisat, se vor apela mai multe metode ajutatoare:</li>
->in primul rand, se citeste linie cu linie din fisierul in care retinem informatiile despre quiz-uri

->se preia id-ul fiecarei intrebari, se apeleaza metoda 'findQuestionById', care returneaza numele intrebarii, in functie de id-ul acesteia

->pentru fiecare intrebare, se creeaza o lista de raspunsuri, prin apelarea metodei 'getAnswersByQuestionName' din clasa Answers, care construieste un obiect de tip Answers, ia informatiile necesare despre acesta din fiser si il pune in lista de raspunsuri

->la string-ul care va fi afisat se adauga infomatiile despre intrebare, se parcurge lista de raspunsuri si si adauga informatiile despre fiecare raspuns
</ul>

#### 9. Raspunderea la un chestionar
##### Apel de sistem
`````
–submit-quizz -u ‘my_username’ -p ‘my_password’ -id ‘quizz_id1’ -answer_id-1 ‘answerid1’ -answwer-id-2 ‘answerid2’ …
`````

<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>daca, in urma apelului metodei 'verifyQuizzById' se intoarce 'false', se afiseaza mesaj de eroare, intrucat nu exista niciun quiz cu acel id in sistem</li>
<li>daca utilizatorul nu a introdus de la tastatura id-ul pentru o intrebare, desi a specificat argumentul -answer_id-1, se va intoarce mesaj de eroare</li>
<li>daca in fisierul in care retinem quiz-urile submise, apare id-ul quiz-ului curent si username-ul celui care vrea sa raspunda, atunci afisam mesaje de eroare, intrucat utilizatorul incearca sa submita acelasi quiz inca odata</li>
<li>daca username-ul de la quiz-ul curent, din fisierul in care retinem quiz-urile este acelasi cu al userului curent, atunci se afiseaza mesaj de eroare, intrucat utilizatorul incearca sa raspunda la propriul quiz</li>
<li>altfel, se modifica argumentul isCompleted al quiz-ului din fisier cu 'true'(se citesc toate informatiile despre quiz-uri din fisier; toate informatiile despre quiz-urile care nu au id-ul quiz-ului curent se retin intr-un string, iar quiz-ul curent se retine intr-un alt string separat si i se mofisica isCompleted cu 'true'; se sterg datele din fisier si se adauga cele 2 stringuri construite)</li>
<li>calculul punctajului:</li>
->pentru fiecare intrebare se calculeaza numarul de raspunsuri corecte si numarul de raspunsuri gresite introduse de utilizator, numarul de raspunsuri corecte si numarul de raspunsuri gresite, care ar fi trebuit introduse de utilizator(daca o intrebare nu are raspuns, atunci nu se aduna nimic la numarul de raspunsuri corecte/gresite introduse de utiilizator)

->calculul raspunsurilor se realizeaza in functie de tipul intrebarii ('single' sau 'multiple'); pentru cele de tip single se verifica o singura data daca id-ul raspunsului introdus de utilizator este continut in stringul care contine id-urile cu raspunsurile corecte; pentru intrebarile de tip 'multiple' se parcurge vectorul care contine id-urile cu raspunsurile utilizatorului si se face aceeasi verificare ca la cele de tip 'single';

->se calculeaza ponderea raspunsurilor corecte introduse de utilizator dupa formula nr_raspunsuri_corecte_introduse / nr_raspunsuri_corecte_total; la fel se procedeaza si pt raspunsurile gresite, numai ca de data aceasta procentajul va fi negativ

->la scorul final se aduna cele 2 ponderi calculate, inmultite cu ponderea unei intrebari din quiz(punctaj maxim / nr_intrebari)

->daca scorul este negativ, atunci se va afisa 0, altfel se va afisa scorul final, rotunjit

<li>in final, se scriu in fisier informatiile despre rezultatele submise si punctajul total</li>

</ul>

#### 10. Stergerea unui chestionar
##### Apel de sistem
`````
–delete-quizz -u ‘my_username’ -p ‘my_password’ -id ‘quizz_id1’
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>pentru stergerea unui quiz se parcurge fisierul, se retin intr-un string toate informatiile despre quiz-urile care nu au id-ul quiz-ului curent, se sterg datele din fisier si se scrie in fisier string-ul construit;</li>
</ul>

#### 11. Afisarea solutiilor unui utilizator la un chestionar
##### Apel de sistem
`````
–get-my-solutions -u ‘my_username’ -p ‘my_password
`````
<ul><li>se verifica, la fel ca mai sus, daca datele de autentificare sunt corecte si valide</li>
<li>se parcurge fisierul in care retinem rezultatele quiz-urilor si se afiseaza toate quiz-urile submise de utilizatorul curent</li>
</ul>

#### 12. Stergerea datelor din aplicatie
##### Apel de sistem
`````
–cleanup-al
`````
<ul><li>se sterg toate datele din toate fisierele</li>
</ul>

# Bonus
#### Cazuri limita
1. Daca un utilizator doreste sa isi afiseze toate solutiile, insa nu a raspuns la niciun quiz, trebuie afisat un mesaj de eroare.
2. Daca un utilizator doreste sa creeze 2 quiz-uri cu nume diferite, dar cu aceleasi intrebari, trebuie afisat un mesaj de eroare.
3. Daca un utilizator doreste sa afiseze detaliile unui quiz, insa nu exista niciun quiz cu acel id, trebuie afisat un mesaj de eroare.

#### Refactorizare comenzi
1. As fi putut construi comenzile astfel incat utilizatorul sa nu fie nevoit sa introduca datele in ordinea specificata in enunt, ci sa le introduca in orice ordine, iar programul sa le recunoasca.
2. In loc sa retin informatiile in fisiere as fi putut sa le retin in alte structuri de date, cum ar fi vectori, liste, etc, pentru a fi mai usor de manipulat si pentru eficienta programului(in loc sa parcurg fisierele de cate ori am nevoie de o informatie).
3. Comenzile introduse de utilizator ar fi putut fi formatate intr-un mod mai usor de scris, intrucat utilizatorul poate gresi destul de usor prin introducerea unui spatiu sau a unui caracter in plus sau in minus.