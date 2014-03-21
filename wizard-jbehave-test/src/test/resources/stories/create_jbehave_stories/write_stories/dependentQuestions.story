Scenario: Open default page

When the user opens the default page

Scenario: Choose template with dependent questions

When the user choose template with name 'Dependent Questions Template'

Scenario: Check that first dependent question is inVisible (Topic 1.1)

Then element with id 'maincontentid-panel_qawes123' is inVisible

Scenario: Check that first dependent question appear (Topic 1.1)

When the checkbox id/name/className 'maincontentid-ywywywuququ-0' is unchecked make it checked
And the checkbox id/name/className 'maincontentid-ywywywuququ-3' is checked make it unchecked
Then element with id 'maincontentid-panel_qawes123' is visible

Scenario: Check that second dependent question is inVisible (Topic 1.1)

Then element with id 'maincontentid-panel_qp123' is inVisible

Scenario: Check that second dependent question appear (Topic 1.1)

When the user fills 'maincontentid-qawes123' field with '22'
And the user clicks on element with id/name/className 'maincontentid-labelIdFor-ywywywuququ'
Then element with id 'maincontentid-panel_qp123' is visible

Scenario: Go to Second Topic

When user click 'Next' button
And user click 'Next' button

Scenario: Check that first dependent question is inVisible (Topic 1.2)

Then element with id 'maincontentid-panel_sksksk626262' is inVisible

Scenario: Check that first dependent question appear (Topic 1.2)

When the user fills 'maincontentid-yqjamzks721' field with 'Show me please!'
And the user clicks on element with id/name/className 'maincontentid-labelIdFor-yqjamzks721'
Then element with id 'maincontentid-panel_sksksk626262' is visible

Scenario: Check that second dependent is inVisible (Topic 1.2)

Then element with id 'maincontentid-panel_qwe123r421' is inVisible

Scenario: Check that second dependent question appear (Topic 1.2)

When chooses text 'CHOOSE 1' from 'maincontentid-sksksk626262' drop-down
Then element with id 'maincontentid-panel_qwe123r421' is visible

Scenario: Go to Page 2

When user click 'Next' button

Scenario: Check that first dependent question is inVisible (Topic 2.1)

When the user fills 'maincontentid-rqewbzla52431_input' field with '30-04-1992'
And clicks on element with id/name/className 'maincontentid-labelIdFor-rqewbzla52431'
Then element with id 'maincontentid-panel_wuwu5353' is inVisible

Scenario: Check that first dependent question is visible (Topic 2.1)

When the user fills 'maincontentid-rqewbzla52431_input' field with '30-04-1991'
And clicks on element with id/name/className 'maincontentid-labelIdFor-rqewbzla52431'
Then element with id 'maincontentid-panel_wuwu5353' is visible

Scenario: Go to Page 1, Topic 1.1

When clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
When clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'

Scenario: Change dependent Question and modal dialog is visible

Then element with id 'maincontentid-dialogDependentQuestion' is inVisible
When the checkbox id/name/className 'maincontentid-ywywywuququ-3' is unchecked make it checked
Then element with id 'maincontentid-dialogDependentQuestion' is visible
