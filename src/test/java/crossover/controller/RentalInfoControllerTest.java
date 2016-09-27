package crossover.controller;

import crossover.config.UnitTestContext;
import crossover.controller.RentalInfoController;
import crossover.errors.RentalInfoNotExistException;
import crossover.models.RentalInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class RentalInfoControllerTest {

    private static final String ERROR_MESSAGE_CODE_EMPTY_TITLE = "NotEmpty.title";
    private static final String ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE = "NotEmpty.todo.title";
    private static final String ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION = "Length.todo.description";

    private static final String ERROR_MESSAGE_EMPTY_TODO_TITLE = "Title cannot be empty.";
    private static final String ERROR_MESSAGE_TOO_LONG_DESCRIPTION = "The maximum length of the description is 500 characters.";

    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";

    private static final String OBJECT_NAME = "todo";

    private static final String SEARCH_TERM = "Foo";

    private RentalInfoController controller;


    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new RentalInfoController();
    }

    @Test
    public void test_success() {
        return;
    }

 /*   @Test
    public void add_AllFieldsOk_ShouldReturnAddedRentalInfo() {
        RentalInfo rentalInfo = new RentalInfo();
        rentalInfo.setCity("TestCity");
        rentalInfo.setCountry("TESTCountry");
        rentalInfo.setCurrency("TestCurrency");
        rentalInfo.setHasAirCondition(true);
        rentalInfo.setType("TestType");
        rentalInfo.setCloseToBeach(false);

        RentalInfo expected =
        when(controller.postRentalInfo(null, rentalInfo)).thenReturn(expected);

        RentalInfoDTO actual = controller.add(dto);

        verify(serviceMock, times(1)).add(dto);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);

        assertRentalInfo(expected, actual);
    }

    @Test(expected = FormValidationError.class)
    public void add_EmptyRentalInfo_ShouldThrowException() throws FormValidationError {
        RentalInfoDTO dto = RentalInfoTestUtil.createDTO(null, "", "");

        controller.add(dto);

        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock, serviceMock);
    }

    @Test(expected = FormValidationError.class)
    public void add_TitleAndDescriptionAreTooLong_ShouldThrowException() throws FormValidationError {
        String description = RentalInfoTestUtil.createStringWithLength(RentalInfo.MAX_LENGTH_DESCRIPTION + 1);
        String title = RentalInfoTestUtil.createStringWithLength(RentalInfo.MAX_LENGTH_TITLE + 1);

        RentalInfoDTO dto = RentalInfoTestUtil.createDTO(null, description, title);

        controller.add(dto);

        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock, serviceMock);
    }

    @Test
    public void deleteById_RentalInfoIsNotFound_ShouldReturnDeletedRentalInfo() throws RentalInfoNotExistException {
        RentalInfo expected = RentalInfoTestUtil.createModel(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION, RentalInfoTestUtil.TITLE);
        when(serviceMock.deleteById(RentalInfoTestUtil.ID)).thenReturn(expected);

        RentalInfoDTO actual = controller.deleteById(RentalInfoTestUtil.ID);

        verify(serviceMock, times(1)).deleteById(RentalInfoTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);

        assertRentalInfo(expected, actual);
    }

    @Test(expected = RentalInfoNotExistException.class)
    public void deleteById_RentalInfoIsNotFound_ShouldThrowException() throws RentalInfoNotExistException {
        when(serviceMock.deleteById(RentalInfoTestUtil.ID)).thenThrow(new RentalInfoNotFoundException(""));

        controller.deleteById(RentalInfoTestUtil.ID);

        verify(serviceMock, times(1)).deleteById(RentalInfoTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);
    }

    @Test
    public void findAll_ShouldReturnRentalInfoList() {
        RentalInfo model = RentalInfoTestUtil.createModel(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION, RentalInfoTestUtil.TITLE);
        List<RentalInfo> expected = createModels(model);

        when(serviceMock.findAll()).thenReturn(expected);

        List<RentalInfoDTO> actual = controller.findAll();

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);

        assertRentalInfos(expected, actual);
    }

    @Test
    public void update_AllFieldsOk_ShouldReturnUpdatedRentalInfo() throws FormValidationError, RentalInfoNotExistException {
        RentalInfoDTO dto = RentalInfoTestUtil.createDTO(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION_UPDATED, RentalInfoTestUtil.TITLE_UPDATED);
        RentalInfo expected = RentalInfoTestUtil.createModel(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION, RentalInfoTestUtil.TITLE);
        when(serviceMock.update(dto)).thenReturn(expected);

        RentalInfoDTO actual = controller.update(dto, RentalInfoTestUtil.ID);

        verify(serviceMock, times(1)).update(dto);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);

        assertRentalInfo(expected, actual);
    }

    @Test(expected = FormValidationError.class)
    public void update_EmptyRentalInfo_ShouldThrowException() throws FormValidationError, RentalInfoNotExistException {
        RentalInfoDTO dto = RentalInfoTestUtil.createDTO(RentalInfoTestUtil.ID, "", "");

        controller.update(dto, RentalInfoTestUtil.ID);

        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock, serviceMock);
    }

    @Test(expected = FormValidationError.class)
    public void update_TitleAndDescriptionAreTooLong_ShouldThrowException() throws FormValidationError, RentalInfoNotExistException {
        String description = RentalInfoTestUtil.createStringWithLength(RentalInfo.MAX_LENGTH_DESCRIPTION + 1);
        String title = RentalInfoTestUtil.createStringWithLength(RentalInfo.MAX_LENGTH_TITLE + 1);

        RentalInfoDTO dto = RentalInfoTestUtil.createDTO(RentalInfoTestUtil.ID, description, title);

        controller.update(dto, RentalInfoTestUtil.ID);

        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock, serviceMock);
    }

    @Test(expected = RentalInfoNotFoundException.class)
    public void update_RentalInfoIsNotFound_ShouldThrowException() throws FormValidationError, RentalInfoNotExistException {
        RentalInfoDTO dto = RentalInfoTestUtil.createDTO(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION, RentalInfoTestUtil.TITLE);
        when(serviceMock.update(dto)).thenThrow(new RentalInfoNotFoundException(""));

        controller.update(dto, RentalInfoTestUtil.ID);

        verify(serviceMock, times(1)).update(dto);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);
    }

    private void assertRentalInfos(List<RentalInfo> expected, List<RentalInfo> actual) {
        assertEquals(expected.size(), actual.size());

        for (int index = 0; index < expected.size(); index++) {
            RentalInfo model = expected.get(index);
            RentalInfo dto = actual.get(index);
            assertRentalInfo(model, dto);
        }
    }



    @Test
    public void search_ShouldReturnRentalInfoList() {
        RentalInfoDocument document = RentalInfoTestUtil.createDocument(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION, RentalInfoTestUtil.TITLE);
        List<RentalInfoDocument> documents = createDocuments(document);

        when(serviceMock.search(SEARCH_TERM)).thenReturn(documents);

        List<RentalInfoDTO> results = controller.search(SEARCH_TERM);

        verify(serviceMock, times(1)).search(SEARCH_TERM);
        verifyNoMoreInteractions(serviceMock);

        assertEquals(documents.size(), results.size());

        for (int index = 0; index < documents.size(); index++) {
            RentalInfoDocument expected = documents.get(index);
            RentalInfoDTO actual = results.get(index);

            assertEquals(Long.valueOf(expected.getId()), actual.getId());
            assertEquals(expected.getTitle(), actual.getTitle());
            assertNull(actual.getDescription());
        }
    }

    private List<RentalInfoDocument> createDocuments(RentalInfoDocument... documents) {
        List<RentalInfoDocument> list = new ArrayList<RentalInfoDocument>();

        for (RentalInfoDocument document: documents) {
            list.add(document);
        }

        return list;
    }

    @Test
    public void findById_RentalInfoIsFound_ShouldReturnRentalInfo() throws RentalInfoNotFoundException {
        RentalInfo expected = RentalInfoTestUtil.createModel(RentalInfoTestUtil.ID, RentalInfoTestUtil.DESCRIPTION, RentalInfoTestUtil.TITLE);
        when(serviceMock.findById(RentalInfoTestUtil.ID)).thenReturn(expected);

        RentalInfoDTO actual = controller.findById(RentalInfoTestUtil.ID);

        verify(serviceMock, times(1)).findById(RentalInfoTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);

        assertRentalInfo(expected, actual);
    }

    @Test(expected = RentalInfoNotFoundException.class)
    public void findById_RentalInfoIsNotFound_ShouldThrowException() throws RentalInfoNotFoundException {
        when(serviceMock.findById(RentalInfoTestUtil.ID)).thenThrow(new RentalInfoNotFoundException(""));

        controller.findById(RentalInfoTestUtil.ID);

        verify(serviceMock, times(1)).findById(RentalInfoTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(localeHolderWrapperMock, messageSourceMock);
    }

    @Test
    public void handleFormValidationError_AllMessagesFound_ShouldReturnFormValidationErrors() {
        FieldError titleError = createFieldError(OBJECT_NAME, FIELD_TITLE, ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE);
        FieldError descriptionError = createFieldError(OBJECT_NAME, FIELD_DESCRIPTION, ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION);

        List<FieldError> errors = new ArrayList<FieldError>();
        errors.add(titleError);
        errors.add(descriptionError);

        FormValidationError validationError = new FormValidationError(errors);

        when(localeHolderWrapperMock.getCurrentLocale()).thenReturn(Locale.US);

        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US)).thenReturn(ERROR_MESSAGE_EMPTY_TODO_TITLE);
        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionError.getArguments(), Locale.US)).thenReturn(ERROR_MESSAGE_TOO_LONG_DESCRIPTION);

        FormValidationErrorDTO dto = controller.handleFormValidationError(validationError);

        verify(localeHolderWrapperMock, times(1)).getCurrentLocale();
        verifyNoMoreInteractions(localeHolderWrapperMock);

        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US);
        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionError.getArguments(), Locale.US);
        verifyNoMoreInteractions(messageSourceMock);

        verifyZeroInteractions(serviceMock);

        List<FieldValidationErrorDTO> fieldErrorDTOs = dto.getFieldErrors();

        assertEquals(2, fieldErrorDTOs.size());

        FieldValidationErrorDTO titleFieldErrorDTO = fieldErrorDTOs.get(0);
        assertEquals(FIELD_TITLE, titleFieldErrorDTO.getPath());
        assertEquals(ERROR_MESSAGE_EMPTY_TODO_TITLE, titleFieldErrorDTO.getMessage());

        FieldValidationErrorDTO descriptionFieldErrorDTO = fieldErrorDTOs.get(1);
        assertEquals(FIELD_DESCRIPTION, descriptionFieldErrorDTO.getPath());
        assertEquals(ERROR_MESSAGE_TOO_LONG_DESCRIPTION, descriptionFieldErrorDTO.getMessage());
    }

    @Test
    public void handleFormValidationError_ErrorMessageIsNotFoundWithFirstErrorCode_ShouldReturnFormValidationError() {
        FieldError titleError = createFieldError(OBJECT_NAME, FIELD_TITLE, ERROR_MESSAGE_CODE_EMPTY_TITLE, ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE);

        List<FieldError> errors = new ArrayList<FieldError>();
        errors.add(titleError);

        FormValidationError validationError = new FormValidationError(errors);

        when(localeHolderWrapperMock.getCurrentLocale()).thenReturn(Locale.US);

        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_EMPTY_TITLE, titleError.getArguments(), Locale.US)).thenReturn(ERROR_MESSAGE_CODE_EMPTY_TITLE);
        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US)).thenReturn(ERROR_MESSAGE_EMPTY_TODO_TITLE);

        FormValidationErrorDTO dto = controller.handleFormValidationError(validationError);

        verify(localeHolderWrapperMock, times(1)).getCurrentLocale();
        verifyNoMoreInteractions(localeHolderWrapperMock);

        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_EMPTY_TITLE, titleError.getArguments(), Locale.US);
        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US);
        verifyNoMoreInteractions(messageSourceMock);

        verifyZeroInteractions(serviceMock);

        List<FieldValidationErrorDTO> fieldErrorDTOs = dto.getFieldErrors();

        assertEquals(1, fieldErrorDTOs.size());

        FieldValidationErrorDTO titleFieldErrorDTO = fieldErrorDTOs.get(0);
        assertEquals(FIELD_TITLE, titleFieldErrorDTO.getPath());
        assertEquals(ERROR_MESSAGE_EMPTY_TODO_TITLE, titleFieldErrorDTO.getMessage());
    }

    @Test
    public void handleFormValidationError_ErrorMessagesAreNotFound_ShouldReturnFormValidationError() {
        FieldError titleError = createFieldError(OBJECT_NAME, FIELD_TITLE, ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE);
        FieldError descriptionError = createFieldError(OBJECT_NAME, FIELD_DESCRIPTION, ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION);

        List<FieldError> errors = new ArrayList<FieldError>();
        errors.add(titleError);
        errors.add(descriptionError);

        FormValidationError validationError = new FormValidationError(errors);

        when(localeHolderWrapperMock.getCurrentLocale()).thenReturn(Locale.US);

        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US)).thenReturn(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE);
        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionError.getArguments(), Locale.US)).thenReturn(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION);

        FormValidationErrorDTO dto = controller.handleFormValidationError(validationError);

        verify(localeHolderWrapperMock, times(1)).getCurrentLocale();
        verifyNoMoreInteractions(localeHolderWrapperMock);

        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US);
        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionError.getArguments(), Locale.US);
        verifyNoMoreInteractions(messageSourceMock);

        verifyZeroInteractions(serviceMock);

        List<FieldValidationErrorDTO> fieldErrorDTOs = dto.getFieldErrors();

        assertEquals(2, fieldErrorDTOs.size());

        FieldValidationErrorDTO titleFieldErrorDTO = fieldErrorDTOs.get(0);
        assertEquals(FIELD_TITLE, titleFieldErrorDTO.getPath());
        assertEquals(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleFieldErrorDTO.getMessage());

        FieldValidationErrorDTO descriptionFieldErrorDTO = fieldErrorDTOs.get(1);
        assertEquals(FIELD_DESCRIPTION, descriptionFieldErrorDTO.getPath());
        assertEquals(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionFieldErrorDTO.getMessage());
    }

    @Test
    public void handleFormValidationError_ErrorMessagesAreNull_ShouldReturnFormValidationErrorWitNullErrorMessages() {
        FieldError titleError = createFieldError(OBJECT_NAME, FIELD_TITLE, ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE);
        FieldError descriptionError = createFieldError(OBJECT_NAME, FIELD_DESCRIPTION, ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION);

        List<FieldError> errors = new ArrayList<FieldError>();
        errors.add(titleError);
        errors.add(descriptionError);

        FormValidationError validationError = new FormValidationError(errors);

        when(localeHolderWrapperMock.getCurrentLocale()).thenReturn(Locale.US);

        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US)).thenReturn(null);
        when(messageSourceMock.getMessage(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionError.getArguments(), Locale.US)).thenReturn(null);

        FormValidationErrorDTO dto = controller.handleFormValidationError(validationError);

        verify(localeHolderWrapperMock, times(1)).getCurrentLocale();
        verifyNoMoreInteractions(localeHolderWrapperMock);

        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_EMPTY_TODO_TITLE, titleError.getArguments(), Locale.US);
        verify(messageSourceMock, times(1)).getMessage(ERROR_MESSAGE_CODE_TOO_LONG_DESCRIPTION, descriptionError.getArguments(), Locale.US);
        verifyNoMoreInteractions(messageSourceMock);

        verifyZeroInteractions(serviceMock);

        List<FieldValidationErrorDTO> fieldErrorDTOs = dto.getFieldErrors();

        assertEquals(2, fieldErrorDTOs.size());

        FieldValidationErrorDTO titleFieldErrorDTO = fieldErrorDTOs.get(0);
        assertEquals(FIELD_TITLE, titleFieldErrorDTO.getPath());
        assertNull(titleFieldErrorDTO.getMessage());

        FieldValidationErrorDTO descriptionFieldErrorDTO = fieldErrorDTOs.get(1);
        assertEquals(FIELD_DESCRIPTION, descriptionFieldErrorDTO.getPath());
        assertNull(descriptionFieldErrorDTO.getMessage());
    }
*/
    private void assertRentalInfo(RentalInfo expected, RentalInfo actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getCountry(), actual.getCountry());

    }

    private FieldError createFieldError(String objectName, String path, String... errorMessageCodes) {
        return new FieldError(objectName,
                path,
                null,
                false,
                errorMessageCodes,
                new Object[]{},
                errorMessageCodes[0]);
    }
}
