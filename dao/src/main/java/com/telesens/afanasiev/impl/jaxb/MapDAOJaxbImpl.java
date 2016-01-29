package com.telesens.afanasiev.impl.jaxb;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.jaxb.schemes.BusNetwork;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class MapDAOJaxbImpl implements MapDAO {

    private String fileName;

    public MapDAOJaxbImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void insertOrUpdate(Map map) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.telesens.afanasiev.impl.jaxb.schemes");
            BusNetwork busNetwork = copyDataFromMap(map);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(busNetwork, new FileWriter(fileName));

        } catch(JAXBException | IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public Collection<Map> getRange(long from, long to){
        return null;
    }

    @Override
    public Map getById(long id) {
        return null;
    }

    @Override
    public void delete(Long id){}

    private BusNetwork copyDataFromMap(Map map) {
        BusNetwork busNetwork = new BusNetwork();
        BusNetwork.Stations stationsData = new BusNetwork.Stations();
        BusNetwork.Arcs arcsData = new BusNetwork.Arcs();
        BusNetwork.Routes routesData = new BusNetwork.Routes();
        BusNetwork.SimpleRoutes simpleRoutesData = new BusNetwork.SimpleRoutes();
        BusNetwork.CircularRoutes circularRoutesData = new BusNetwork.CircularRoutes();

        busNetwork.setStations(stationsData);
        busNetwork.setArcs(arcsData);
        busNetwork.setRoutes(routesData);
        busNetwork.setSimpleRoutes(simpleRoutesData);
        busNetwork.setCircularRoutes(circularRoutesData);

        Collection<Station> stations =  map.getAllStations();
        Collection<Arc<Station>> arcs = map.getAllArcs();

        setStationsData(stations, busNetwork);
        setArcsData(arcs, busNetwork);
        setRouteData(map.getForwardRoutes(), "FORWARD", busNetwork);
        setRouteData(map.getBackRoutes(), "BACK", busNetwork);
        setRouteData(map.getCircularRoutes(), "FORWARD", busNetwork);
        setCircularRouteData(map.getCircularRoutes(), busNetwork);
        setSimpleRouteData(map.getPairsRoutes(), busNetwork);

        busNetwork.setName(map.getName());
        busNetwork.setDescription(map.getDescribe());

        return busNetwork;
    }

    private void setStationsData(Collection<Station> stations, BusNetwork busNetwork) {
        BusNetwork.Stations.Station stationData;
        for (Station station : stations) {
            stationData = new BusNetwork.Stations.Station();
            stationData.setId(station.getId());
            stationData.setName(station.getName());

            busNetwork.getStations().getStation().add(stationData);
        }
    }

    private void setArcsData(Collection<Arc<Station>> arcs, BusNetwork busNetwork) {
        BusNetwork.Arcs.Arc arcData;
        for (Arc<Station> arc : arcs) {
            arcData = new BusNetwork.Arcs.Arc();
            arcData.setId(arc.getId());
            arcData.setDuration(arc.getDuration());
            arcData.setNodeLeftId(arc.getNodeLeft().getId());
            arcData.setNodeRightId(arc.getNodeRight().getId());

            busNetwork.getArcs().getArc().add(arcData);
        }
    }

    private void setRouteData(Collection<Route<Station>> routes, String direct, BusNetwork busNetwork) {
        BusNetwork.Routes.Route routData;
        for (Route<Station> route : routes) {
            routData = new BusNetwork.Routes.Route();

            routData.setId(route.getId());
            routData.setNumber(route.getNumber());
            routData.setDirect(direct);
            routData.setCost(route.getCost().doubleValue());
            routData.setFirstNodeId(route.getFirstNode().getId());
            routData.setDescription(route.getDescription());

            setArcLinkData(route.getSequenceArcs(), routData);

            busNetwork.getRoutes().getRoute().add(routData);
        }
    }

    private void setArcLinkData(Collection<Arc<Station>> arcs, BusNetwork.Routes.Route routeEl) {
        BusNetwork.Routes.Route.ArcsLink routeLinkData;

        for (Arc<Station> arc : arcs) {
            routeLinkData = new BusNetwork.Routes.Route.ArcsLink();

            routeLinkData.setArcId(arc.getId());

            routeEl.getArcsLink().add(routeLinkData);
        }
    }

    private void setCircularRouteData(Collection<Route<Station>> routes, BusNetwork busNetwork) {
        BusNetwork.CircularRoutes.RouteLink circRouteData;
        for (Route<Station> route : routes) {
            circRouteData = new BusNetwork.CircularRoutes.RouteLink();
            circRouteData.setRouteId(route.getId());

            busNetwork.getCircularRoutes().getRouteLink().add(circRouteData);
        }
    }

    private void setSimpleRouteData(Collection<RoutePair<Station>> pairs, BusNetwork busNetwork) {
        BusNetwork.SimpleRoutes.Pair pairData;
        long i = 1;
        for (RoutePair<Station> pair : pairs) {
            pairData = new BusNetwork.SimpleRoutes.Pair();
            pairData.setId(i++);
            pairData.setRouteForwardId(pair.getForwardRoute().getId());
            pairData.setRouteBackId(pair.getBackRoute().getId());

            busNetwork.getSimpleRoutes().getPair().add(pairData);
        }
    }
}
