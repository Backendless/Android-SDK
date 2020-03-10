package com.backendless.persistence;

import com.backendless.Backendless;
import com.backendless.IDataStore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;


/**
 * <p>This is an implementation of the Java Collection interface enabling retrieval and iteration over a collection of objects stored in a Backendless data table.</p>
 * <p>The interface methods returning data are mapped to the corresponding Backendless APIs.</p>
 * <p>The Iterator returned by the implementation lets you access either all objects from the data table or a subset determined by a where clause. Additionally, the implementation can work with data streams.</p>
 *
 * <ul><u>The collection has <i>two modes of operation</i><u>:
 * <li><b>persisted</b> - all retrieved objects are saved locally to enable faster access in future iterations. The persisted data is shared between all iterators returned by the collection. To enable this mode use the "preservedData" parameter.
 * <li><b>transient</b> - every iterator returned by the collection works with a fresh data collection returned from the server.
 * </ul>
 *
 * @param <T> the type of your entity. Make sure it is properly mapped with {@code Backendless.Data.mapTableToClass( String tableName, Class<T> entityClass )}
 */
public class BackendlessDataCollection<T extends BackendlessDataCollection.Identifiable<T>> implements Collection<T>
{
  public static interface Identifiable<T>
  {
    String getObjectId();

    void setObjectId( String id );
  }

  private Class<T> entityType;
  private IDataStore<T> iDataStore;
  private String slice;
  private LinkedHashMap<String, T> preservedData; // if null, the mode is 'transient'
  private int size;
  private boolean isLoaded = false;

  public BackendlessDataCollection( Class<T> entityType )
  {
    this( entityType, false );
  }

  public BackendlessDataCollection( Class<T> entityType, String slice )
  {
    this( entityType, false, slice );
  }

  public BackendlessDataCollection( Class<T> entityType, boolean preserveIteratedData )
  {
    this( entityType, preserveIteratedData, "" );
  }

  public BackendlessDataCollection( Class<T> entityType, boolean preserveIteratedData, String slice )
  {
    this.entityType = entityType;
    this.slice = (slice == null) ? "" : slice;
    this.iDataStore = Backendless.Data.of( this.entityType );

    if( preserveIteratedData )
      this.preservedData = new LinkedHashMap<>();

    this.size = getRealSize();
  }

  /**
   * @return <b>query</b>, which limits the data set, retrieved from the table.
   */
  public String getSlice()
  {
    return slice;
  }

  /**
   * @return true, if this collection was created with parameter <b>preserveIteratedData</b>
   */
  public boolean isPersisted()
  {
    return this.preservedData != null;
  }

  /**
   * Only for the <b>persisted</b> mode.
   *
   * @return the number of elements that preserved locally.
   */
  public int getPersistedSize()
  {
    if( this.preservedData == null )
      throw new IllegalStateException( "This collection is not persisted." );

    return this.preservedData.size();
  }

  /**
   * Only for the <b>persisted</b> mode.
   *
   * @return true, if the current collection ihas been fully loaded from Backendless.
   */
  public boolean isLoaded()
  {
    if( this.preservedData == null )
      throw new IllegalStateException( "This collection is not persisted." );

    return this.isLoaded;
  }

  /**
   * If this collection is in the <b>persisted</b> mode, this operation deletes all locally saved data and udates the size.
   * The operation has only local effect.
   */
  public void invalidateState()
  {
    if( this.preservedData != null )
    {
      this.preservedData.clear();
      this.isLoaded = false;
    }

    this.size = getRealSize();
  }

  /**
   * Only for the <b>persisted</b> mode.
   * Fills up this collection with the values from the Backendless table.
   */
  public void populate()
  {
    if( this.preservedData == null )
      throw new IllegalStateException( "This collection is not persisted." );

    Iterator<T> iter = this.iterator();

    while( iter.hasNext() )
      iter.next();
  }

  private int getRealSize()
  {
    return this.iDataStore.getObjectCount( DataQueryBuilder.create().setWhereClause( this.slice ) );
  }

  private void checkObjectType( Object o )
  {
    if( this.entityType != o.getClass() )
      throw new IllegalArgumentException( o.getClass() + " is not a type of objects contained in this collection." );
  }

  private void checkObjectTypeAndId( Object o )
  {
    if( this.entityType != o.getClass() )
      throw new IllegalArgumentException( o.getClass() + " is not a type of objects contained in this collection." );

    String objectId = ((T) o).getObjectId();

    if( objectId == null )
      throw new IllegalArgumentException( "'objectId' is null." );
  }

  private String getQuery( String id )
  {
    String query = "objectId='" + id + "'";
    query = (this.slice.isEmpty()) ? query : this.slice + " and " + query;
    return query;
  }

  private String getQuery( Collection<T> objs, boolean exclude )
  {
    String firstPart = exclude ? "objectId not in (" : "objectId in (";

    StringBuilder sb = new StringBuilder( firstPart );

    for( T obj : objs )
      sb.append( '\'' ).append( obj.getObjectId() ).append( '\'' ).append( ',' );

    sb.replace( sb.length() - 1, sb.length(), ")" );

    String query = sb.toString();
    query = (this.slice.isEmpty()) ? query : this.slice + " and " + query;
    return query;
  }

  private String getQueryByIds( Collection<String> ids, boolean exclude )
  {
    String firstPart = exclude ? "objectId not in (" : "objectId in (";

    StringBuilder sb = new StringBuilder( firstPart );

    for( String id : ids )
      sb.append( '\'' ).append( id ).append( '\'' ).append( ',' );

    sb.replace( sb.length() - 1, sb.length(), ")" );

    String query = sb.toString();
    query = (this.slice.isEmpty()) ? query : this.slice + " and " + query;
    return query;
  }

  /**
   * <p>Returns object by its '{@code objectId}'. Takes into account slice (where clause).
   * If this collection is <i>persisted</i> and fully loaded, than no api-calls will be performed.
   *
   * @param objectId
   * @return
   */
  public T getById( String objectId )
  {
    if( this.preservedData != null && this.isLoaded )
      return this.preservedData.get( objectId );

    DataQueryBuilder queryBuilder = DataQueryBuilder.create().setWhereClause( this.getQuery( objectId ) );
    return (T) this.iDataStore.find( queryBuilder );
  }

  @Override
  public Iterator<T> iterator()
  {
    return new BackendlessDataCollectionIterator();
  }

  @Override
  public int size()
  {
    return this.size;
  }

  @Override
  public boolean remove( Object o )
  {
    this.checkObjectTypeAndId( o );

    boolean result = false;

    if( this.preservedData != null )
      result = this.preservedData.remove( ((T) o).getObjectId() ) != null;

    result |= this.iDataStore.remove( this.getQuery( ((T) o).getObjectId() ) ) != 0;

    return result;
  }

  @Override
  public boolean removeAll( Collection<?> c )
  {
    for( Object element : c )
      this.checkObjectTypeAndId( element );

    boolean result = false;

    if( this.preservedData != null )
    {
      for( T entity : (Collection<T>) c )
        result = this.preservedData.remove( entity.getObjectId() ) != null;
    }

    result |= this.iDataStore.remove( this.getQuery( (Collection<T>) c, false ) ) != 0;

    return result;
  }

  @Override
  public boolean isEmpty()
  {
    return this.size == 0;
  }

  /**
   * If this collection is in the <i>persisted</i> mode and is fully loaded, this method will result in no additonal API calls to the server.
   *
   * @param o object to check if the collection has it
   * @return true if the object is in the collection, false otherwise
   */
  @Override
  public boolean contains( Object o )
  {
    this.checkObjectTypeAndId( o );

    boolean result = false;

    if( this.preservedData != null )
      result = this.preservedData.containsKey( ((T) o).getObjectId() );

    if( this.isLoaded )
      return result;

    DataQueryBuilder queryBuilder = DataQueryBuilder.create().setWhereClause( this.getQuery( ((T) o).getObjectId() ) );
    result |= this.iDataStore.getObjectCount( queryBuilder ) != 0;

    return result;
  }

  /**
   * If this collection is in the <i>persisted</i> mode and is fully loaded, this method will not result in any additional API calls to the server.
   *
   * @return
   */
  @Override
  public T[] toArray()
  {
    if( this.preservedData != null && this.isLoaded )
      return this.preservedData.values().toArray( (T[]) Array.newInstance( this.entityType, this.preservedData.size() ) );

    ArrayList<T> list = new ArrayList<>();

    Iterator<T> iter = this.iterator();

    while( iter.hasNext() )
      list.add( iter.next() );

    return (T[]) list.toArray();
  }

  /**
   * If this collection is in the <i>persisted</i> mode and is fully loaded, this method will not result in any additional API calls to the server.
   *
   * @return
   */
  @Override
  public <T1> T1[] toArray( T1[] a )
  {
    Class arrayType = a.getClass().getComponentType();

    if( this.entityType != arrayType )
      throw new IllegalArgumentException( arrayType + " is not a type objects of which are contained in this collection." );

    return (T1[]) this.toArray();
  }

  /**
   * If this collection is a 'slice' of data from Backendless table, then after every <i>add</i> operation an API call to the server will be performed to check that the new saved object doesn't violate the 'slice' condition.
   *
   * @param t
   * @return
   */
  @Override
  public boolean add( T t )
  {
    this.checkObjectType( t );

    T savedEntity = this.iDataStore.save( t );

    if( this.slice != null )
    {
      DataQueryBuilder queryBuilder = DataQueryBuilder.create().setWhereClause( this.getQuery( savedEntity.getObjectId() ) );
      if( this.iDataStore.getObjectCount( queryBuilder ) == 0 )
      {
        this.iDataStore.remove( this.getQuery( savedEntity.getObjectId() ) );
        return false;
      }
    }

    if( this.preservedData != null )
      preservedData.put( savedEntity.getObjectId(), savedEntity );

    this.size++;
    return true;
  }

  /**
   * If this collection is in the <i>persisted</i> mode and is fully loaded, this method will not result in any additional API calls to the server.
   *
   * @param c
   * @return
   */
  @Override
  public boolean containsAll( Collection<?> c )
  {
    for( Object element : c )
      this.checkObjectTypeAndId( element );

    Collection<T> collection = (Collection<T>) c;

    if( this.preservedData != null && this.isLoaded )
    {
      Set<String> listId = new HashSet<>();
      for( T obj : collection )
        listId.add( obj.getObjectId() );

      return this.preservedData.keySet().containsAll( listId );
    }

    DataQueryBuilder queryBuilder = DataQueryBuilder.create().setWhereClause( this.getQuery( collection, false ) );
    return this.iDataStore.getObjectCount( queryBuilder ) == c.size();
  }

  @Override
  public boolean addAll( Collection<? extends T> c )
  {
    for( Object element : c )
      this.checkObjectType( element );

    List<String> listId = this.iDataStore.create( (List<T>) c );

    if( this.slice != null )
    {
      DataQueryBuilder queryBuilder = DataQueryBuilder.create().setWhereClause( this.getQueryByIds( listId, false ) );
      if( this.iDataStore.getObjectCount( queryBuilder ) != listId.size() )
      {
        this.iDataStore.remove( this.getQueryByIds( listId, false ) );
        return false;
      }
    }

    if( this.preservedData != null )
    {
      for( int i = 0; i < c.size(); i++ )
      {
        T obj = ((List<T>) c).get( i );
        obj.setObjectId( listId.get( i ) );
        preservedData.put( obj.getObjectId(), obj );
      }
    }

    this.size += listId.size();
    return true;
  }

  @Override
  public boolean retainAll( Collection<?> c )
  {
    for( Object element : c )
      this.checkObjectTypeAndId( element );

    Set<String> listId = new HashSet<>();
    for( T obj : (Collection<T>) c )
      listId.add( obj.getObjectId() );

    boolean result = this.iDataStore.remove( this.getQueryByIds( listId, true ) ) != 0;

    if( this.preservedData != null )
    {
      Iterator<String> idIterator = this.preservedData.keySet().iterator();
      while( idIterator.hasNext() )
      {
        if( !listId.contains( idIterator.next() ) )
        {
          result = true;
          idIterator.remove();
        }
      }
    }

    return result;
  }

  /**
   * Clears all data in the remote table and in local collection (if mode is <i>persisted</i>).
   * Takes into account the slice (where clause) that was set on creation.
   */
  @Override
  public void clear()
  {
    this.iDataStore.remove( this.slice );
    invalidateState();
  }

  /**
   * Takes into account only 'entityType' and 'slice'.
   *
   * @param o
   * @return
   */
  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;
    if( !(o instanceof BackendlessDataCollection) )
      return false;
    BackendlessDataCollection<?> that = (BackendlessDataCollection<?>) o;
    return Objects.equals( entityType, that.entityType ) && Objects.equals( slice, that.slice );
  }

  /**
   * Takes into account only 'entityType' and 'slice'.
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    return Objects.hash( entityType, slice );
  }

  public class BackendlessDataCollectionIterator implements Iterator<T>
  {
    private static final int pageSize = 100;

    private DataQueryBuilder queryBuilder;
    private int currentPosition;
    private List<T> currentPageData;
    private List<T> nextPageData;
    private Iterator<T> persistedIterator;
    private T[] loadedData;

    private BackendlessDataCollectionIterator()
    {
      if( BackendlessDataCollection.this.size() < 1 )
        return;

      if( BackendlessDataCollection.this.isLoaded )
      {
        persistedIterator = BackendlessDataCollection.this.preservedData.values().iterator();
        return;
      }

      this.currentPosition = 0;
      this.queryBuilder = DataQueryBuilder.create().setWhereClause( BackendlessDataCollection.this.slice ).setPageSize( pageSize );

      if( BackendlessDataCollection.this.preservedData == null )
      {
        this.currentPageData = BackendlessDataCollection.this.iDataStore.find( this.queryBuilder );
        this.nextPageData = BackendlessDataCollection.this.iDataStore.find( this.queryBuilder.prepareNextPage() );
      }
      else
      {
        T[] array = (T[]) Array.newInstance( BackendlessDataCollection.this.entityType, BackendlessDataCollection.this.preservedData.size() );
        loadedData = BackendlessDataCollection.this.preservedData.values().toArray( array );

        // load first page
        this.currentPageData = new ArrayList<>();
        boolean fullPage = this.loadNextPageUsingLocalDataIfPresent( 0, this.currentPageData );
        if( !fullPage )
          return;

        // load second page
        this.nextPageData = new ArrayList<>();
        this.loadNextPageUsingLocalDataIfPresent( 1, this.nextPageData );
      }
    }

    @Override
    public boolean hasNext()
    {
      boolean hasNext;

      if( this.persistedIterator != null )
        hasNext = this.persistedIterator.hasNext();
      else
        hasNext = ((currentPageData != null && currentPosition % pageSize < currentPageData.size()) || (nextPageData != null && !nextPageData.isEmpty()));

      if( !hasNext )
      {
        this.currentPageData = this.nextPageData = null;
        this.persistedIterator = null;
        this.queryBuilder = null;
      }

      return hasNext;
    }

    @Override
    public T next()
    {
      if( this.persistedIterator != null )
        return this.persistedIterator.next();

      if( this.currentPageData == null )
        throw new NoSuchElementException();

      int indexOnPage = currentPosition++ % pageSize;

      T result = null;

      if( indexOnPage < currentPageData.size() )
        result = currentPageData.get( indexOnPage );

      if( indexOnPage == currentPageData.size() - 1 )
        getNextPage();

      return result;
    }

    private void getNextPage()
    {
      if( currentPageData == null || nextPageData == null || nextPageData.isEmpty() )
      {
        currentPageData = nextPageData = null;
        return;
      }

      currentPageData = nextPageData;
      if( currentPageData.size() < pageSize )
      {
        this.nextPageData = null;
        return;
      }

      if( BackendlessDataCollection.this.preservedData == null )
      {
        this.nextPageData = BackendlessDataCollection.this.iDataStore.find( this.queryBuilder.prepareNextPage() );

        if( this.nextPageData.size() < pageSize )
          BackendlessDataCollection.this.size = currentPosition + this.currentPageData.size() + this.nextPageData.size();
      }
      else
      {
        // load next page
        this.nextPageData = new ArrayList<>();
        int nextPageNumber = currentPosition / pageSize + 1;
        this.loadNextPageUsingLocalDataIfPresent( nextPageNumber, this.nextPageData );
      }
    }

    private boolean loadNextPageUsingLocalDataIfPresent( int pageNumber, List<T> target )
    {
      int startLoadIndex = pageNumber * pageSize;
      int lastLoadIndex = startLoadIndex + pageSize;

      if( loadedData != null )
      {
        int tmpLastIndex = (loadedData.length < lastLoadIndex) ? loadedData.length : lastLoadIndex;

        for( int i = startLoadIndex; i < tmpLastIndex; i++ )
          target.add( loadedData[ i ] );

        startLoadIndex = tmpLastIndex;
      }

      boolean fullPage = true;
      if( target.size() < pageSize )
      {
        loadedData = null;
        fullPage = loadPartialData( startLoadIndex, lastLoadIndex - startLoadIndex, target );
      }

      return fullPage;
    }

    private boolean loadPartialData( int pOffset, int pPageSize, List<T> target )
    {
      this.queryBuilder.setOffset( pOffset ).setPageSize( pPageSize );
      List<T> lackingObjects = BackendlessDataCollection.this.iDataStore.find( this.queryBuilder );
      this.queryBuilder.setPageSize( pageSize );
      target.addAll( lackingObjects );

      // save data to the main collection
      for( T obj : lackingObjects )
        BackendlessDataCollection.this.preservedData.put( obj.getObjectId(), obj );

      boolean fullPage = lackingObjects.size() == pPageSize;
      if( !fullPage )
      {
        BackendlessDataCollection.this.size = pOffset + pPageSize - 1;
        BackendlessDataCollection.this.isLoaded = true;
      }

      return fullPage;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException( "remove" );
    }
  }
}
