Scenario: Open default page

When the user opens the default page

Scenario: Start the wizard template

When the user choose template with name 'Test All Components'

Scenario: TEXT

When the user fills 'maincontentid-text_question' field with 'Change the text input'
When clicks on element with id/name/className 'maincontentid-panel_text_question_content'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-text_question' has attribute value 'Change the text input'

Scenario: PARAGRAPHTEXT

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user fills 'maincontentid-paragraphtext_question' field with 'Change the paragraph text input'
When clicks on element with id/name/className 'maincontentid-panel_paragraphtext_question_content'
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

Scenario: CHECKBOXES

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the checkbox id/name/className 'maincontentid-checkboxes_question-0' is unchecked make it checked
And the checkbox id/name/className 'maincontentid-checkboxes_question-1' is checked make it unchecked
And the checkbox id/name/className 'maincontentid-checkboxes_question-2' is checked make it unchecked
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then checkbox with id 'maincontentid-checkboxes_question-0' is checked

Scenario: CHOOSEFROMLIST

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When chooses text 'Option 1' from 'maincontentid-choosefromlist_question' drop-down
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then in 'maincontentid-choosefromlist_question' drop-down is selected text 'Option 1' option

Scenario: GRID

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the checkbox id/name/className 'maincontentid-gridcell_grid_question_4_input' is unchecked make it checked
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then checkbox with id 'maincontentid-gridcell_grid_question_4_input' is checked

Scenario: DATE

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user fills 'maincontentid-date_question_input' field with '12-12-2014'
When clicks on element with id/name/className 'maincontentid-panel_date_question_content'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-date_question_input' has attribute value '12-12-2014'
When the user fills 'maincontentid-date_question_input' field with '2222222222222'
When clicks on element with id/name/className 'maincontentid-panel_date_question_content'

Scenario: Click other questions to the end

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'

Scenario: go to the first page

When clicks on element with id/name/className 'dateStubb-page_one'

Scenario: TEXT edit previous

When the user fills 'maincontentid-text_question' field with 'Just another text input'
When clicks on element with id/name/className 'maincontentid-panel_text_question_content'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-text_question' has attribute value 'Just another text input'

Scenario: PARAGRAPHTEXT edit previous

When clicks on element with id/name/className 'leftmenuid-paragraphtext_topic'
When the user fills 'maincontentid-paragraphtext_question' field with 'Just another paragraph text input'
When clicks on element with id/name/className 'maincontentid-panel_paragraphtext_question_content'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-paragraphtext_question' has attribute value 'Just another paragraph text input'

Scenario: MULTIPLE CHOICE edit previous

When clicks on element with id/name/className 'leftmenuid-multiplechoice_topic'
When chooses text 'Option 2' from 'maincontentid-multiplechoice_question' drop-down
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-multiplechoice_question' has attribute value 'Option 2'

Scenario: CHECKBOXES edit previous

When clicks on element with id/name/className 'leftmenuid-checkboxes_topic'
When the checkbox id/name/className 'maincontentid-checkboxes_question-1' is unchecked make it checked
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then checkbox with id 'maincontentid-checkboxes_question-1' is checked

Scenario: CHOOSEFROMLIST edit previous

When clicks on element with id/name/className 'leftmenuid-choosefromlist_topic'
When chooses text 'Option 3' from 'maincontentid-choosefromlist_question' drop-down
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then in 'maincontentid-choosefromlist_question' drop-down is selected text 'Option 3' option

Scenario: GRID edit previous

When clicks on element with id/name/className 'leftmenuid-grid_topic'
When the checkbox id/name/className 'maincontentid-gridcell_grid_question_2_input' is unchecked make it checked
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then checkbox with id 'maincontentid-gridcell_grid_question_2_input' is checked

Scenario: DATE edit previous

When clicks on element with id/name/className 'leftmenuid-date_topic'
When the user fills 'maincontentid-date_question_input' field with '15-03-1998'
When clicks on element with id/name/className 'maincontentid-panel_date_question_content'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When the user clicks on element with id/name/className 'navigationButtonsForm-buttonidprev'
Then element 'maincontentid-date_question_input' has attribute value '15-03-1998'
When clicks on element with id/name/className 'maincontentid-panel_date_question_content'

Scenario: go to the second page

When clicks on element with id/name/className 'dateStubb-page_two'

Scenario: click all topics on the second page

When clicks on element with id/name/className 'leftmenuid-range_topic'
When clicks on element with id/name/className 'leftmenuid-time_topic'
When clicks on element with id/name/className 'leftmenuid-fileupload_topic'

Scenario: return to default page

When the user clicks on element with id/name/className 'navigationButtonsForm-buttonid'
When clicks on element with id/name/className 'logo'
Then check that current URL is 'http://localhost:8080/wizard-web/'