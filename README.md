GodStemIt
=========

System rozpoznawania emocji (*sentiment analysis*) w języku polskim.

W jego skład wchodzą poszczególne elementy:

- baza danych (MySQL)
- stemmer (Java JDK 1.8)
- analiza i uczenie maszynowe (R)

##Wstęp
Większość algorytmów text-miningowych do poprawnego działania wymaga przeprowadzeniu na tekście **stemmingu** (czyli
redukcji słowa do jego rdzenia albo formy podstawowej). W innych językach jej wykonanie jest standardem. Z uwagi na skomplikowanie
języka polskiego ciężko jest znaleźć narzędzie radzące sobie z tą operacją. W aplikacji wykorzystano system [Morfologic] (http://morfologik.blogspot.com/)
w wersji 1.9.0 (wersja z dnia 2014-02-21) stworzone przez dr Marcina Miłkowskiego i dr Dawida Weissa.
 
###Baza danych
 Wykorzystanie bazy MySQL jako źródła danych niesie następujące korzyści:
 
 - brak konieczności eksportu i importu danych w różnych formatach (np. do csv),
 - ujednolicony interfejs,
 - brak redundancji informacji
 
 
 Schemat tabeli przechowującej opinie wygląda następująco:
 
id | original                                                                     | processed | class | error 
-- | ---------------------------------------------------------------------------- | --------- | ----- | -------
 1 | Ten produkt bardzo pozytywnie mnie zaskoczył. Na pewno zakupię go ponownie   | NULL      |     1 |       
 2 | Nie polecam tego sklepu. Fatalna obsługa                                     | NULL      |     0 |       


W kolumnie *original* przechowywana jest oryginalna treść komunikatu, w kolumnie *processed* wartość uzyskana po wykonaniu
stemmingu, *class* określa czy opinia jest pozytywna bądź negatywna, natomiast *error* wskazuje na bład w obliczeniach 
(prawdopodbnie problem z danymi w kolumnie *original*)

###Stemmer
todo: Tutaj informacje ad. stemmera

###Analiza danych
todo: I część poświęcona R