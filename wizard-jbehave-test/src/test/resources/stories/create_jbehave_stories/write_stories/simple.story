
Scenario: Open default page

When the user opens the default page

Scenario: Test wizard web (choose Simple Form Template 1)

When the user choose template with name 'Simple Form Template 1'
Then element id 'maincontentid' has text 'Page 1 - Test group 1'

Scenario: Choose all Topics on all Pages and check output text in Template 1

When user choose Page with text 'Page 1'
Then element id 'maincontentid' has text 'Page 1 - Test group 1'

When user choose Topic with text 'Test group 1'
Then element id 'maincontentid' has text 'Page 1 - Test group 1'

When user choose Topic with text 'Test group 2'
Then element id 'maincontentid' has text 'Page 1 - Test group 2'

When user choose Topic with text 'Test group 3'
Then element id 'maincontentid' has text 'Page 1 - Test group 3'

When user choose Page with text 'Page 2'
Then element id 'maincontentid' has text 'Page 2 - Super Puper Topic 1'

When user choose Topic with text 'Super Puper Topic 1'
Then element id 'maincontentid' has text 'Page 2 - Super Puper Topic 1'

When user choose Topic with text 'Super Puper Topic 2'
Then element id 'maincontentid' has text 'Page 2 - Super Puper Topic 2'

Scenario: Back on the default page

When user click on logo
Then check that current URL is 'http://localhost:8080/wizard-web/'

Scenario: Test wizard web (choose Simple Form Template 2)

When the user choose template with name 'Simple Form Template 2'
Then element id 'maincontentid' has text 'Page 1 - Topic bla-bla From page 1'

Scenario: Choose all Topics on all Pages and check output text in Template 1

When user choose Page with text 'Page 1'
Then element id 'maincontentid' has text 'Page 1 - Topic bla-bla From page 1'

When user choose Topic with text 'Topic bla-bla From page 1'
Then element id 'maincontentid' has text 'Page 1 - Topic bla-bla From page 1'

When user choose Topic with text 'Topic bla-bla 2 From page 1'
Then element id 'maincontentid' has text 'Page 1 - Topic bla-bla 2 From page 1'

When user choose Page with text 'Page 2'
Then element id 'maincontentid' has text 'Page 2 - Topic First from page 2'

When user choose Topic with text 'Topic First from page 2'
Then element id 'maincontentid' has text 'Page 2 - Topic First from page 2'

When user choose Topic with text 'Topic Second from Page 2'
Then element id 'maincontentid' has text 'Page 2 - Topic Second from Page 2'

When user choose Page with text 'Page 3'
Then element id 'maincontentid' has text 'Page 3 - Topic 1 from Page 3'

When user choose Topic with text 'Topic 1 from Page 3'
Then element id 'maincontentid' has text 'Page 3 - Topic 1 from Page 3'

When user choose Topic with text 'Topic 2 from Page 3'
Then element id 'maincontentid' has text 'Page 3 - Topic 2 from Page 3'

When user choose Topic with text 'Topic 3 from Page 3'
Then element id 'maincontentid' has text 'Page 3 - Topic 3 from Page 3'

Scenario: Back on the default page

When user click on logo
Then check that current URL is 'http://localhost:8080/wizard-web/'

Scenario: Test wizard web (choose Simple Form Template 3)

When the user choose template with name 'Simple Form Template 3'
Then element id 'maincontentid' has text 'Page 1 - Prime Topic ***'

Scenario: Choose all Topics on all Pages and check output text in Template 1

When user choose Page with text 'Page 1'
Then element id 'maincontentid' has text 'Page 1 - Prime Topic ***'

When user choose Topic with text 'Prime Topic ***'
Then element id 'maincontentid' has text 'Page 1 - Prime Topic ***'

When user choose Topic with text 'Prime Topic 2 ***'
Then element id 'maincontentid' has text 'Page 1 - Prime Topic 2 ***'

When user choose Topic with text 'Prime Topic 3 ***'
Then element id 'maincontentid' has text 'Page 1 - Prime Topic 3 ***'

When user choose Page with text 'Page 2'
Then element id 'maincontentid' has text 'Page 2 - Mega Topic 1'

When user choose Topic with text 'Mega Topic 1'
Then element id 'maincontentid' has text 'Page 2 - Mega Topic 1'

When user choose Topic with text 'Mega Topic 2'
Then element id 'maincontentid' has text 'Page 2 - Mega Topic 2'

When user choose Topic with text 'Mega Topic 3'
Then element id 'maincontentid' has text 'Page 2 - Mega Topic 3'

Scenario: Back on the default page

When user click on logo
Then check that current URL is 'http://localhost:8080/wizard-web/'