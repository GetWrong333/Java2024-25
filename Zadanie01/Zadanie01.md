Krótki opis
Zadanie polega na wykonywaniu prostych operacji arytmetycznych. Wyzwaniem będzie sposób, w jaki będą one zlecane.

Sposób kodowania
Informacja o operacji do wykonania zostanie zakodowana w ciągu bitów, które będą kolejno, bit po bicie, przekazywane przez użytkownika.

Dane zostaną przekazane w następującym formacie:

aaaabbbbbbccddddddd
Gdzie:

część "a" będzie długości 4 bitów. Będzie ona kodować długość (liczbę bitów) segmentów b i d. Dozwolone są długości od 2 do 15.
część "b" to pierwszy argument
część "d" to drugi argument
część "c" to dwu-bitowy kod operacji: 00 - dodawanie, 01 - odejmowanie, 10 - mnożenie, 11 - dzielenie.
Czyli: długość części "a" i "c" jest stała (odpowiednio: 4 i 2 bity), długość segmentów "b" i "d" trzeba zdekodować na podstawie bitów części "a".

Argumenty

Argumentami operacji arytmetycznej będą liczby całkowite. Zostaną one zakodowane następująco:

Pierwszy bit koduje znak - 0 liczba dodatnia, 1 liczba ujemna.
Pozostałe bity to liczba całkowita zapisana binarnie.
Przykłady:

dla długości segmentu "4": 1011 oznacza liczbę -3
dla długości segmentu "6": 001111 oznacza liczbę +15
Przykład danych wejściowych
0101 11111 00 01110 
   5  -15   +  14 
Wynik to -1.

Odbieranie danych:
W trakcie przekazywania danych, gdy poprzedniego wyniku jeszcze nie było, wynikiem jest błąd
W trakcie przekazywania nowych danych i jeśli poprzedni wynik już odebrano, wynikiem jest błąd
W trakcie przekazywania nowych danych i jeśli poprzedniego wyniku nie odebrano, wynikiem jest poprzedni wynik.
Błąd kodowany jako -2_000_000_000 (zakładamy, że żaden rachunek do takiej liczby nie doprowadzi).

Wynik można odebrać tylko jeden raz. Najpóźniej można go odebrać po przekazaniu przedostatniego bitu nowego ciągu. Po odebraniu wyniku, do czasu wygenerowania kolejnego, program zwraca błąd.

Przekazanie ostatniego bitu danych powoduje nadpisanie bufora - poprzedniego wyniku już nie będzie można odebrać.

Przykład działania programu
Poniżej przedstawiono wynik programu w trakcie przekazywania kolejnych bitów.

-----------------czas------------->
   pierwszy ciąg                     drugi ciąg
0101 11111 00 01110                0010 00 00 01

Scenariusz 1: 
|------błąd-------|          wynik -1          | wynik 1

Scenariusz 2: 
|------błąd-------| odebrano wynik -> błąd     | wynik 1

Scenariusz 3: 
|------błąd-------| nie odebrano wyniku        | wynik 1
Scenariusze:

Wynik pierwszego ciągu (-1) można odebrać najpóźniej po przekazaniu przedostatniego bitu kolejnego ciągu. Gdy dostarczono cały nowy ciąg, wynik zmienia się na nowy.
Odebranie wyniku powoduje, że kolejne pytania o wynik (do czasu wygenerowania kolejnego) zwracają błąd.
Nieodebrany wynik przepada. Jeśli dostarczymy kolejny ciąg do końca, nowy wynik zajmie miejsce poprzedniego. Starego wyniku odebrać już nie będzie można.
We wszystkich przypadkach pytania o wynik w trakcie przekazywania początkowych danych zawsze zwracają błąd.

Zakodowane scenariusze
Scenariusz typ 1:

input(1) // przedostatni bit pierwszego ciagu
wynik() -> błąd
input(0) // ostatni bit pierwszego ciągu
input(0) // pierwszy bit drugiego ciągu
input(0) // drugi bit drugiego ciągu
wynik() -> -1
Scenariusz typ 2:

input(1) // przedostatni bit pierwszego ciagu
wynik() -> błąd
input(0) // ostatni bit pierwszego ciągu
input(0) // pierwszy bit drugiego ciągu
input(0) // drugi bit drugiego ciągu
wynik() -> -1
input(1) // trzeci bit drugiego ciągu
wynik() -> błąd
input(0) // czwarty bit drugiego ciągu
wynik() -> błąd
[...]
input(1) // ostatni bit drugiego ciągu
wynik() -> 1
Scenariusz typ 3:

input(1) // przedostatni bit pierwszego ciagu
wynik() -> błąd
input(0) // ostatni bit pierwszego ciągu
input(0) // pierwszy bit drugiego ciągu
input(0) // drugi bit drugiego ciągu
input(1) // trzeci bit drugiego ciągu
input(0) // czwarty bit drugiego ciągu
[...]
input(1) // ostatni bit drugiego ciągu
wynik() -> 1 // poprzedniego wyniku już nie ma, zwracamy aktualny
wynik() -> błąd // nowego wyniku jeszcze nie ma a ten, który był, został już odebrany
Szablon kodu
Poniżej szablon kodu, do którego należy wpisać rozwiązanie. Nagłówków metod, które umieściłem w szablonie nie wolno zmieniać. Klasa musi pozostać bezpakietowa.

class Zadanie01 {
  private static final int BLAD = -2_000_000_000;
  
  public Zadanie01() {
  }
  
  public void input( int bit ) {
  }
  
  public int wynik( ) {
    
  }
}
Do szablonu można dodawać własne metody. Przeznaczenie metod z szablonu:

input() - za pomocą tej metody wprowadzamy kolejne bity. bit może przyjąć wyłącznie wartości 0 i 1.
wynik() - jak sama nazwa wskazuje, to metoda do pobrania wyniku działania programu.
Zadanie01(), czyli konstruktor - to tego konstruktora użyję do utworzenia obiektu. Możecie Państwo wpisać do niego swój kod.
Uprzedzając ewentualne pytania:

W programie można umieścić main, ale ja z niego nie będę korzystać - będę mieć własny.
Dane do Państwa klasy będą dostarczane przez mój program. Nie będę ich wpisywać z klawiatury i nie oczekuję aby program o takie dane mnie pytał. Program ma przetwarzać wyłącznie dane przekazane przez input.
Innych konstruktorów (nawet jeśli takie będą) nie użyję. Obiekt zostanie utworzony tak:
Zadanie01 zadanie = new Zadanie01();
Wynik zwracany jest jako zwykła liczba całkowita. Nie należy go przekazywać bit-po-bicie.
Dane do obliczeń nie spowodują dzielenia przez zero, przekroczenia zakresu itd.
Nie należy stosować pól statycznych - każdy z obiektów ma działać niezależnie od pozostałych.
Działanie programu się nie kończy - nie wiadomo ile ciągów dostarczy użytkownik
Oczywiście, podane powyżej ciągi to tylko przykłady. Program musi poradzić sobie z każdym poprawnym użyciem. Zakładamy, że użytkownik będzie używać programu poprawnie.