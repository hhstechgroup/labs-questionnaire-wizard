Scenario: Open default page

When the user opens the default page

Scenario: Start the wizard template

When the user choose template with name 'Test All Components'

Scenario: TEXT

When the user fills 'maincontentid-text_question' field with 'Change the text input'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-text_question' has attribute value 'Change the text input'

Scenario: PARAGRAPHTEXT

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user fills 'maincontentid-paragraphtext_question' field with 'Change the paragraph text input'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-paragraphtext_question' has attribute value 'Change the paragraph text input'

Scenario: MULTIPLE CHOICE

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When chooses text 'Option 3' from 'maincontentid-multiplechoice_question' drop-down
When chooses text 'Option 1' from 'maincontentid-multiplechoice_question' drop-down
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-multiplechoice_question' has attribute value 'Option 1'

Scenario: return to default page

When clicks on element with id/name/className 'logo'
Then check that current URL is 'http://localhost:8080/wizard-web/'