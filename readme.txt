do zaimplementowania:

* DynamicRetailer -> DynamicRetailerProcessor - dokonczyc obliczanie znizek
* co ze zbieraniem punktow (LoyalityAccount->points)
* implementacja modulu do zmiany tierLevel
* zbudowac mechanizm priorytetyzacji RuleExecution
* ujednolicic RQ do tworzenia Rule w DynamicRetailer
* zaimplementowac TradeEngineAdapter

* co z walutami?
* czy najpierw skupiamy sie na działaniu ale bez zniżek czy odrazu implementujemy zniżki
* co ze zbieraniem pkt przez klienta (kiedy)

* TradeEngineAdapter chyba musi wszystko z pozostalych downline'ow override-owac zeby latwiej wprowadzac zmiany

* przegladanie swojej historii przez customera (in progress)
* ustalic feature'y z Monika



* przy update product trzeba utworzyć nowy product a stary dezaktywować o ile go ktoś kupił, chodzi o historię produktów
* przeliczanie walut w TradeEngineAdapter
