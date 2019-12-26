package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.form.VungTrungChuyenForm;
import com.havaz.transport.api.model.DistanceDTO;
import com.havaz.transport.api.model.TcDspVungTrungChuyenDTO;
import com.havaz.transport.api.model.TcVungTrungChuyenDTO;
import com.havaz.transport.api.model.VtcDTO;
import com.havaz.transport.api.repository.VungTrungChuyenRepositoryCustom;
import com.havaz.transport.api.service.VungTrungChuyenService;
import com.havaz.transport.api.utils.SecurityUtils;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VungTrungChuyenServiceImpl implements VungTrungChuyenService {

    private static final Logger log = LoggerFactory.getLogger(VungTrungChuyenServiceImpl.class);

    @Autowired
    private VungTrungChuyenRepositoryCustom vungTrungChuyenRepositoryCustom;

    //tc_vtt_id, tc_vtt_name, tc_vtt_code, created_by, last_updated_by, created_date, AsText(tc_vtt_content), tc_average_speed
    @Override
    public List<TcDspVungTrungChuyenDTO> getDataByName(String tenVung) {
        List<Object[]> listVtt = new ArrayList<>();
            listVtt = vungTrungChuyenRepositoryCustom.getDataByName(tenVung);
        if (listVtt == null || listVtt.size() == 0) {
            return new ArrayList<>();
        }
        List<TcDspVungTrungChuyenDTO> listVttDto = new ArrayList<>();
        for (Object[] vtt : listVtt) {
            TcDspVungTrungChuyenDTO vttDsp = new TcDspVungTrungChuyenDTO();
            vttDsp.setTcVttId(vtt[0] == null ? 0 : Integer.parseInt(vtt[0].toString()));
            vttDsp.setTcVttName(vtt[1] == null ? "" : vtt[1].toString());
            vttDsp.setTcVttCode(vtt[2] == null ? "" : vtt[2].toString());
            vttDsp.setCreatedBy(vtt[3] == null ? "" : vtt[3].toString());
            vttDsp.setCreatedDate(vtt[5] == null ? "" : vtt[5].toString());
            vttDsp.setTcVttContent(vtt[6] == null ? "" : vtt[6].toString());
            vttDsp.setTcVAverageSpeed(vtt[7] == null ? "" : vtt[7].toString());
            vttDsp.setStatus(vtt[8] == null ? 0 : Integer.parseInt(vtt[8].toString()));
            listVttDto.add(vttDsp);
        }
        return listVttDto;
    }

    @Override
    @Transactional
    public void createOrUpdate(VungTrungChuyenForm vungTrungChuyenForm) {
        try {
            TcVungTrungChuyenDTO vttDto = new TcVungTrungChuyenDTO();
            vttDto.setTcConfirmedTime(vungTrungChuyenForm.getConfirmedTime());
            vttDto.setTcVttNote(vungTrungChuyenForm.getNote());
            String tenS = vungTrungChuyenForm.getTen();
            String codeS = vungTrungChuyenForm.getCode();
            //TODO validate input parameter
            if (vungTrungChuyenForm == null) {
                throw new TransportException("Cần điền đủ thông tin");
            }
            if (tenS == null|| tenS.isEmpty()) {
                throw new TransportException("Vui lòng nhập tên vùng");
            }

            if (codeS == null|| codeS.isEmpty()) {
                throw new TransportException("Vui lòng nhập mã vùng");
            }
            if(vungTrungChuyenForm.getFileContent() != null) {
                InputStream is = vungTrungChuyenForm.getFileContent().getInputStream();
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(is));

                //add file upload to String builder
                String str;
                StringBuilder buf = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    buf.append(str);
                }
                br.close();
                String html = buf.toString();
                Document doc = Jsoup.parse(html, "", Parser.xmlParser());
                StringBuilder cood = new StringBuilder();
                cood.append("POLYGON((");
                //Todo: Set Center Point and return Polygon Area
                cood.append(getCoordinates(doc, vttDto));
                cood.append("))");
//            String fileName = vungTrungChuyenForm.getFileContent().getOriginalFilename();
                vttDto.setTcVttContent(cood.toString().replace(", ))", "))"));
            } else {
                vttDto.setTcVttContent(null);
            }
            vttDto.setTcVttCode(codeS);
            vttDto.setTcVttName(tenS);
            vttDto.setCreatedDate(LocalDateTime.now());
            vttDto.setLastUpdatedDate(LocalDateTime.now());
            vttDto.setStatus(vungTrungChuyenForm.getStatus());
            if (vungTrungChuyenForm.getId() > 0) {
                vttDto.setTcVttId(vungTrungChuyenForm.getId());
            }
            String avengerSpeedS = vungTrungChuyenForm.getAverageSpeed();
            if (avengerSpeedS != null && !avengerSpeedS.isEmpty()) {
                vttDto.setTcVAverageSpeed(Double.parseDouble(avengerSpeedS));
            }
            //TODO get from session and set this information
            if (SecurityUtils.getCurrentUserLogin() > 0) {
                vttDto.setCreatedBy(SecurityUtils.getCurrentUserLogin());
                vttDto.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin());
            } else {
                vttDto.setCreatedBy(0);
                vttDto.setLastUpdatedBy(0);
            }
            vttDto.setStatus(vungTrungChuyenForm.getStatus());
            //insert to DB
            if (vungTrungChuyenForm.getId() > 0) {
                vungTrungChuyenRepositoryCustom.updatePolygon(vttDto);
            } else {
                vungTrungChuyenRepositoryCustom.addPolygon(vttDto);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TransportException(e);
        }
    }

    private void setCenterPoint(Element point, TcVungTrungChuyenDTO vtcDTO) {
        String[] latAndLong = point.html().split(",");
        vtcDTO.setTcCentroidLat(Double.parseDouble(latAndLong[0]));
        vtcDTO.setTcCentroidLong(Double.parseDouble(latAndLong[1]));
    }

    private String getCoordinates(Document doc, TcVungTrungChuyenDTO vtcDTO) {
        Elements e = doc.select("coordinates");
        String coordinates = new String();
        if( e.size() > 1) {
            coordinates = e.get(0).html();
            //Todo: Set Center Point
            setCenterPoint(e.get(1), vtcDTO);
        } else {
            coordinates = e.html();
        }

        coordinates = coordinates.replace(",0", "-");
        coordinates = coordinates.replace(",", ":");
        coordinates = coordinates.replace("-", ", ");
        coordinates = coordinates.replace(", ))", "))");
        coordinates = coordinates.replace(":", " ");
        coordinates = coordinates.replace("\n", "");
        return coordinates;
    }

    @Override
    public int checkExistsPoint(Double latPoint, Double longPoint) {
        //TODO validate input parameter
        if (latPoint == null) {
            log.warn("Lỗi set vùng trung chuyển: Dữ liệu tọa độ latitude không chính xác");
            return 0;
        }
        if (longPoint == null) {
            log.warn("Lỗi set vùng trung chuyển: Dữ liệu tọa độ longitude không chính xác");
            return 0;
        }
        //TODO input data
        try {
            //Get data from DB
            List<TcDspVungTrungChuyenDTO> listVtt = this.getDataByName("");
            Path2D path = new Path2D.Double();
            for (TcDspVungTrungChuyenDTO tcVtt : listVtt) {
                String dataInDB = tcVtt.getTcVttContent();
                //POLYGON((105.802 21.0301, 105.799 21.0268, 105.798 21.0219, 105.802 21.0301))
                //Latitude: vĩ độ 21.0301
                //Longitude: kinh độ 105.802
                dataInDB = dataInDB.replace("POLYGON((", Strings.EMPTY);
                dataInDB = dataInDB.replace("))", Strings.EMPTY);
                String[] listPoint = dataInDB.split(Strings.COMMA);
                for (int i = 0; i < listPoint.length; i++) {
                    String[] pointX = listPoint[i].split(StringUtils.SPACE);
                    if (i == 0) {
                        path.moveTo(Double.valueOf(pointX[0]), Double.valueOf(pointX[1]));
                    }
                    path.lineTo(Double.valueOf(pointX[0]), Double.valueOf(pointX[1]));
                }
                path.closePath();
                if (path.contains(longPoint, latPoint)) {
                    return tcVtt.getTcVttId();
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
            log.warn(e.getMessage(), e);
            return 0;
        }
        return 0;
    }

    @Override
    public DistanceDTO getDistance(String latX, String longX, String latY, String longY) {
        String distance ="";
        String KEY = "AIzaSyBWFiKgsP3ggaMR6_E3okhkQvOeXoNkgJo";
        if (latX == null || latX.isEmpty()) {
            throw new TransportException("Dữ liệu tọa độ latX không chính xác");
        }
        if (longX == null || longX.isEmpty()) {
            throw new TransportException("Dữ liệu tọa độ longX không chính xác");
        }
        if (latY == null || latY.isEmpty()) {
            throw new TransportException("Dữ liệu tọa độ latX không chính xác");
        }
        if (longY == null || longY.isEmpty()) {
            throw new TransportException("Dữ liệu tọa độ longX không chính xác");
        }
        //TODO input data
        //20.99418, 105.76398
        //21.08459, 105.8091
        double inputLatX = Double.parseDouble(latX);
        double inputLongX = Double.parseDouble(longX);
        double inputLatY = Double.parseDouble(latY);
        double inputLongY = Double.parseDouble(longY);
//        /*//TODO CALL google API via RestTemplate
//        String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592&key=AIzaSyBWFiKgsP3ggaMR6_E3okhkQvOeXoNkgJo";
          String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+inputLatX+","+inputLongX+"&destinations="+inputLatY+","+inputLongY+"&key=AIzaSyBWFiKgsP3ggaMR6_E3okhkQvOeXoNkgJo";
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        JSONParser parser = new JSONParser();
        try {
            response = new OkHttpClient().newCall(request).execute();
            Object obj = parser.parse(response.body().string());
            JSONObject jsonobj=(JSONObject)obj;
            JSONArray dist=(JSONArray)jsonobj.get("rows");
            JSONObject obj2 = (JSONObject)dist.get(0);
            JSONArray disting=(JSONArray)obj2.get("elements");
            JSONObject obj3 = (JSONObject)disting.get(0);
            JSONObject obj4=(JSONObject)obj3.get("distance");
            JSONObject obj5=(JSONObject)obj3.get("duration");
            log.debug("{}", obj4.get("text"));
            log.debug("{}", obj5.get("text"));
            DistanceDTO distanceDTO = new DistanceDTO();
            distanceDTO.setDistance(obj4.get("text").toString());
            distanceDTO.setTime(obj5.get("text").toString());
            return  distanceDTO;
        } catch (ParseException | IOException e) {
            log.error(e.getMessage(), e);
            throw new TransportException(e);
        }

    }

    @Override
    public List<VtcDTO> findAllByStatus(Integer status) {
        return vungTrungChuyenRepositoryCustom.findAllByStatus(status);
    }
}
