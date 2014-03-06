Scenario: open default page

When the user opens the default page

Scenario: chose template

When the user choose template with name 'testPreviousButton'

Scenario: next button click for go to end of template
When clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When clicks on element with id/name/className 'navigationButtonsForm-buttonid'


Scenario: then we want navigate back with previous button and check question changes

When clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element id/name/className 'maincontentid-labelIdFor-pageTwoGroupOne' has text 'QUESTION FOUR'

When clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element id/name/className 'maincontentid-labelIdFor-pageOneGroupThree' has text 'QUESTION THREE'

When clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element id/name/className 'maincontentid-labelIdFor-pageOneGroupTwo' has text 'QUESTION TWO'

When clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element id/name/className 'maincontentid-labelIdFor-pageOneGroupOne' has text 'QUESTION ONE'



