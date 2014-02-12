
Scenario: Open default page

When the user opens the default page

Scenario: Test wizard web (choose First Template)

When the user choose template with name 'Simple Form Template 1'
Then element id 'maincontentid' has text 'Page 1 - Test group 1'

Scenario: Choose all Topics on all Pages and check output text

W