package csed.edu.alexu.eg.virtualbookshelf;

import android.util.Log;

import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import csed.edu.alexu.eg.virtualbookshelf.models.BookFilter.FilterDataByAttribute;
import csed.edu.alexu.eg.virtualbookshelf.models.BookFilter.FilterDataByLocation;
import csed.edu.alexu.eg.virtualbookshelf.models.BookFilter.FilterDataContext;
import csed.edu.alexu.eg.virtualbookshelf.models.UserFunctionality.EditShelf;
import csed.edu.alexu.eg.virtualbookshelf.utility.AddVolumeToShelf;
import csed.edu.alexu.eg.virtualbookshelf.utility.Constants;
import csed.edu.alexu.eg.virtualbookshelf.utility.EditFactory;
import csed.edu.alexu.eg.virtualbookshelf.utility.RemoveVolumeFromShelf;
import csed.edu.alexu.eg.virtualbookshelf.utility.ShowVolumesInBookshelf;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ExampleUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Books books;

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterDataByLocationInvalidLocation() {
        FilterDataContext filterData = new FilterDataByLocation();
        filterData.filterData("ALF1", books);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterDataByLocationGiveNullValue() {
        FilterDataContext filterData = new FilterDataByLocation();
        filterData.filterData(null, books);
    }

    @Test
    public void testFilterDataByLocation() throws IOException {
        FilterDataContext filterData = new FilterDataByLocation();
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        when(books.bookshelves().volumes().list(anyString(), anyString()).execute()).thenReturn(new Volumes());
        assertNotNull(filterData.filterData("ALF", books));
    }

    @Test
    public void testFilterDataByAttribute() throws IOException {
        FilterDataContext filterData = new FilterDataByAttribute();
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        when(books.volumes().list(anyString()).execute()).thenReturn(new Volumes());
        assertNotNull(filterData.filterData("intitle:Harry", books));
    }

    @Test(expected = RuntimeException.class)
    public void testFilterDataByAttributeGiveNullValue() {
        FilterDataContext filterData = new FilterDataByAttribute();
        filterData.filterData(null, books);
    }

    @Test(expected = RuntimeException.class)
    public void testAddVolumeToBookshelfWithInvalidParameters() {
        EditShelf editShelf = new EditShelf();
        editShelf.AddVolumeToShelf(null, null, books);
    }

    @Test(expected = RuntimeException.class)
    public void testClearVolumeToBookshelfWithInvalidParameters() {
        EditShelf editShelf = new EditShelf();
        editShelf.clearShelf(null, books);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteVolumeToBookshelfWithInvalidParameters() {
        EditShelf editShelf = new EditShelf();
        editShelf.deleteFromShelf(null, null, books);
    }

    @Test(expected = RuntimeException.class)
    public void testCallingFunctionGetEditFuncWithoutSettingBooks() {
        EditFactory.getInstance().getEditFun("EditShelf");
    }

    @Test(expected = RuntimeException.class)
    public void testAddVolumeToBookshelfWithNullParameters() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        AddVolumeToShelf volumeToShelf = new AddVolumeToShelf(books);
        volumeToShelf.doFunctionality(null, null);
    }

    @Test(expected = RuntimeException.class)
    public void testAddVolumeToBookshelfWithExtraParameters() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        AddVolumeToShelf volumeToShelf = new AddVolumeToShelf(books);
        volumeToShelf.doFunctionality("0", "1", "2");
    }

    @Test
    public void testAddVolumeToBookshelf() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        AddVolumeToShelf volumeToShelf = new AddVolumeToShelf(books);
        assertNull(volumeToShelf.doFunctionality("Favorites", "1"));
    }

    @Test
    public void testAddVolumeToBookshelfDeepCall() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        EditShelf volumeToShelf = new EditShelf();
        volumeToShelf.AddVolumeToShelf("Favorites", "1", books);
    }

    @Test(expected = RuntimeException.class)
    public void testAddVolumeToBookshelfWithInvalidShelfName() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        AddVolumeToShelf volumeToShelf = new AddVolumeToShelf(books);
        assertNull(volumeToShelf.doFunctionality("Soso", "1"));
    }



    ///////////////////////////////////////////////////////////////////////
    @Test(expected = RuntimeException.class)
    public void testShowVolumesInBookshelfWithNullParameters() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        AddVolumeToShelf volumeToShelf = new AddVolumeToShelf(books);
        volumeToShelf.doFunctionality(null);
    }

    @Test(expected = RuntimeException.class)
    public void testShowVolumesInBookshelfWithExtraParameters() {
        Books books = Mockito.mock(Books.class, Mockito.RETURNS_DEEP_STUBS);
        AddVolumeToShelf volumeToShelf = new AddVolumeToShelf(books);
        volumeToShelf.doFunctionality("0", "1");
    }

    @Test(expected = RuntimeException.class)
    public void testShowVolumesInBookshelf1() {
        ShowVolumesInBookshelf volumeToShelf = new ShowVolumesInBookshelf(EditFactory.getInstance().getBooks());
        assertNotNull(volumeToShelf.doFunctionality(Constants.FAVOURITE));
    }

    @Test(expected = RuntimeException.class)
    public void testShowVolumesInBookshelf2() {
        ShowVolumesInBookshelf volumeToShelf = new ShowVolumesInBookshelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality(Constants.READ).getTotalItems() > 0);
    }

    @Test(expected = RuntimeException.class)
    public void testShowVolumesInBookshelf3() {
        ShowVolumesInBookshelf volumeToShelf = new ShowVolumesInBookshelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality(Constants.WISH_LIST).getTotalItems() > 0);
    }

    @Test(expected = RuntimeException.class)
    public void testShowVolumesInBookshelfWithInvalidShelfId() {
        ShowVolumesInBookshelf volumeToShelf = new ShowVolumesInBookshelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality("toto:soso").getTotalItems() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveVolumeFromShelfWithExtraParameters() {
        RemoveVolumeFromShelf volumeToShelf = new RemoveVolumeFromShelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality("0", "1234", "12").getTotalItems() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveVolumeFromShelfWithNullParameters() {
        RemoveVolumeFromShelf volumeToShelf = new RemoveVolumeFromShelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality(null, "1234").getTotalItems() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testClearVolumeFromShelfWithExtraParameters() {
        RemoveVolumeFromShelf volumeToShelf = new RemoveVolumeFromShelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality("0", "1234", "12").getTotalItems() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testClearVolumeFromShelfWithNullParameters() {
        RemoveVolumeFromShelf volumeToShelf = new RemoveVolumeFromShelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality(null).getTotalItems() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testClearVolumeFromShelf() {
        RemoveVolumeFromShelf volumeToShelf = new RemoveVolumeFromShelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality("0").getTotalItems() == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveVolumeFromShelf() {
        RemoveVolumeFromShelf volumeToShelf = new RemoveVolumeFromShelf(EditFactory.getInstance().getBooks());
        assertTrue(volumeToShelf.doFunctionality("0", "1").getTotalItems() == 0);
    }
}