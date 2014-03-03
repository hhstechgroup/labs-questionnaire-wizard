Scenario: Open default page

When the user opens the default page

Scenario: Test wizard web

When the user choose template with name 'ExemplaryWelcome'

Scenario: First input


When the user fills 'maincontentid-er34werwe' field with 'Petr'
Then element 'maincontentid-er34werwe' has attribute value 'Petr'

When the user fills 'maincontentid-er34werwe' field with 'Helen'
Then element 'maincontentid-er34werwe' has attribute value 'Helen'

When the user fills 'maincontentid-er34werwe' field with 'Jack'
Then element 'maincontentid-er34werwe' has attribute value 'Jack'


Scenario: Check drop-and-down

When choose drop-down with id 'maincontentid-oio8en9' and set value 'Five'

Scenario: Test calendar

When the user fills 'maincontentid-mmkk54fdg_input' field with '01-11-2004'
Then element 'maincontentid-mmkk54fdg_input' has attribute value '01-11-2004'

When the user fills 'maincontentid-mmkk54fdg_input' field with '05-07-2012'
Then element 'maincontentid-mmkk54fdg_input' has attribute value '05-07-2012'

Scenario: Click Next button

When clicks on element with id/name/className 'j_idt21-buttonid'

Scenario: Multiple chooise

When chooses text 'Male' from 'maincontentid-eryeyhg65' drop-down
Then element 'maincontentid-eryeyhg65' has attribute value 'Male'

When chooses text 'Female' from 'maincontentid-eryeyhg65' drop-down
Then element 'maincontentid-eryeyhg65' has attribute value 'Female'

Scenario: Checkbox

When the checkbox id/name/className 'maincontentid-retre657-0' is unchecked make it checked
And the checkbox id/name/className 'maincontentid-retre657-1' is unchecked make it checked
And the checkbox id/name/className 'maincontentid-retre657-2' is unchecked make it checked

Scenario: paragraph text

When the user fills 'maincontentid-fdg4w56335' field with 'Dream Team'
Then element 'maincontentid-fdg4w56335' has attribute value 'Dream Team'

When the user fills 'maincontentid-fdg4w56335' field with 'Team B'
Then element 'maincontentid-fdg4w56335' has attribute value 'Team B'

Scenario:Back first

When clicks on element with id/name/className 'leftmenuid-j_id1'
When clicks on element with id/name/className 'leftmenuid-j_id1'
Then element 'maincontentid-er34werwe' has attribute value 'Jack'

Scenario: return to default page

When clicks on element with id/name/className 'logo'
Then check that current URL is 'http://localhost:8080/wizard-web/'









