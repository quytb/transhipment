package com.havaz.transport.api.service;

import com.havaz.transport.api.form.location.Point;

import java.util.List;

public interface LocationService {

    double getDistanFromAPI(Point pointStart, Point pointEnd);

    double getDurationFromAPI(Point pointStart, Point pointEnd);

    List<Double> getDistanceAndDurationFromAPI(Point pointStart, Point pointEnd);

    List<Point> getPointBetweenMultiplePoint(List<Point> points);
}
