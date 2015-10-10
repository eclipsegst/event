package com.zhaolongzhong.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "momentApi",
        version = "v1",
        resource = "moment",
        namespace = @ApiNamespace(
                ownerDomain = "backend.zhaolongzhong.com",
                ownerName = "backend.zhaolongzhong.com",
                packagePath = ""
        )
)
public class MomentEndpoint {

    private static final Logger logger = Logger.getLogger(MomentEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Moment.class);
    }

    /**
     * Returns the {@link Moment} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Moment} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "moment/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Moment get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Moment with ID: " + id);
        Moment moment = ofy().load().type(Moment.class).id(id).now();
        if (moment == null) {
            throw new NotFoundException("Could not find Moment with ID: " + id);
        }
        return moment;
    }

    /**
     * Inserts a new {@code Moment}.
     */
    @ApiMethod(
            name = "insert",
            path = "moment",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Moment insert(Moment moment) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that moment.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(moment).now();
        logger.info("Created Moment with ID: " + moment.getId());

        return ofy().load().entity(moment).now();
    }

    /**
     * Updates an existing {@code Moment}.
     *
     * @param id     the ID of the entity to be updated
     * @param moment the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Moment}
     */
    @ApiMethod(
            name = "update",
            path = "moment/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Moment update(@Named("id") Long id, Moment moment) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(moment).now();
        logger.info("Updated Moment: " + moment);
        return ofy().load().entity(moment).now();
    }

    /**
     * Deletes the specified {@code Moment}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Moment}
     */
    @ApiMethod(
            name = "remove",
            path = "moment/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Moment.class).id(id).now();
        logger.info("Deleted Moment with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "moment",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Moment> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Moment> query = ofy().load().type(Moment.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Moment> queryIterator = query.iterator();
        List<Moment> momentList = new ArrayList<Moment>(limit);
        while (queryIterator.hasNext()) {
            momentList.add(queryIterator.next());
        }
        return CollectionResponse.<Moment>builder().setItems(momentList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Moment.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Moment with ID: " + id);
        }
    }
}