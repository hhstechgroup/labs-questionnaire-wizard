
Scenario: Open default page

When the user opens the default page
Then the user is brought to the page with 'Football.ua — Новости футбола. Всё о футболе: новости, обзоры, результаты матчей, фото — football.ua' title

Scenario: Try click

When the user dibil write name'$as'

Scenario: Change page

When the user clicks link with text 'Англия'
Then the user is brought to the page with 'Англия — Новости, Премьер-лига, результаты матчей, расписание, турнирная таблица — football.ua' title

Scenario: Test Score

When the user clicks link with text '5:1'
Then the user is brought to the page with 'Ливерпуль 5:1 Арсенал / Англия. Премьер-лига, 25 тур — онлайн, анонс, статистика, отчёт — football.ua' title



When the user opens the default page
Then the user is brought to the page with 'PrimeFaces - ShowCase' title


When the user clicks link with text 'BooleanButton'
And press 'Submit' button
Then wait for element 'form:dialog' is visible
When the user close popup with id 'form:dialog'
