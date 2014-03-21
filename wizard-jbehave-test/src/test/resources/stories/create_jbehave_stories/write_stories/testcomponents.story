Scenario: Open default page

When the user opens the default page

Scenario: Start the wizard template

When the user choose template with name 'Test All Components'

Scenario: change values for the components

When the user fills 'maincontentid-text_question' field with 'Change the text input'
Then element 'maincontentid-text_question' has attribute value 'Change the text input'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
And the user fills 'maincontentid-paragraphtext_question' field with 'Change the paragraph text input'
Then element 'maincontentid-paragraphtext_question' has attribute value 'Change the paragraph text input'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
And chooses text 'Option 1' from 'maincontentid-multiplechoice_question' drop-down
And chooses text 'Option 3' from 'maincontentid-multiplechoice_question' drop-down
Then element 'maincontentid-multiplechoice_question' has attribute value 'Option 3'

Scenario: check the saved values

When clicks on element with id/name/className 'leftmenuid-text_topic'
Then element 'maincontentid-text_question' has attribute value 'Change the text input'

When clicks on element with id/name/className 'leftmenuid-paragraphtext_topic'
Then element 'maincontentid-paragraphtext_question' has attribute value 'Change the paragraph text input'

When clicks on element with id/name/className 'leftmenuid-multiplechoice_topic'
Then element 'maincontentid-multiplechoice_question' has attribute value 'Option 3'

Scenario: return to default page

When clicks on element with id/name/className 'logo'
Then check that current URL is 'http://localhost:8080/wizard-web/'