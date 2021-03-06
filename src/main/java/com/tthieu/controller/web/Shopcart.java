package com.tthieu.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tthieu.model.ProductModel;
import com.tthieu.service.ICategoryService;
import com.tthieu.service.IProductService;
import com.tthieu.utils.HttpUtil;
import com.tthieu.utils.SessionUtil;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/shopcart"})
public class Shopcart extends HttpServlet {

    @Inject
    IProductService productService;

    @Inject
    ICategoryService categoryService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/html;charset=UTF-8");
        String action = req.getParameter("action");
        StringBuilder url = new StringBuilder("/views/web/shopcart/");

        List<ProductModel> shopcart = (List<ProductModel>) SessionUtil.getInstance().getValue(req, "SHOPCART");

        if (action != null) {
            if (action.equals("edit")) {
                int id = Integer.parseInt(req.getParameter("id"));
                for (ProductModel item : shopcart) {
                    if (item.getId() == id) {
                        req.setAttribute("model", item);
                        break;
                    }
                }
                url.append("edit.jsp");
            }
        } else {
            if (shopcart != null && shopcart.size() != 0) {
                double total = 0.0;
                for (ProductModel item : shopcart) {
                    total += item.getPrice() * item.getCount();
                }
                req.setAttribute("listProduct", shopcart);
                req.setAttribute("total", new BigDecimal(total));

            }
            else {
                req.setAttribute("message", "message");
            }

            url.append("list.jsp");
        }

        req.setAttribute("listCategory", categoryService.findAll());
        req.setAttribute("shopcart",1);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(url.toString());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        ProductModel model = HttpUtil.of(req.getReader()).toModel(ProductModel.class);

        List<ProductModel> shopcart = (List<ProductModel>) SessionUtil.getInstance().getValue(req, "SHOPCART");
        if (shopcart != null) {

            int flag = 0;
            for (ProductModel item : shopcart) {
                if (item.getId() == model.getId()) {
                    item.setCount(item.getCount() + 1);
                    flag++;
                    break;
                }
            }
            if (flag == 0) {
                ProductModel modelResult = productService.findOne(model.getId());
                modelResult.setCount(1);
                shopcart.add(modelResult);
            }
            SessionUtil.getInstance().removeValue(req, "SHOPCART");
        } else {
            shopcart = new ArrayList<>();
            ProductModel modelResult = productService.findOne(model.getId());
            modelResult.setCount(1);
            shopcart.add(modelResult);
        }

        SessionUtil.getInstance().putValue(req, "SHOPCART", shopcart);

//        for (ProductModel item : shopcart) {
//            System.out.println("ID:" + item.getId() + " " + "count:" + item.getCount() + " ");
//        }
//        System.out.println("////");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getOutputStream(), 1);

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        ProductModel model = HttpUtil.of(req.getReader()).toModel(ProductModel.class);

        List<ProductModel> shopcart = (List<ProductModel>) SessionUtil.getInstance().getValue(req, "SHOPCART");

        for (ProductModel item : shopcart) {
            if (item.getId() == model.getId()) {
                item.setCount(model.getCount());
                break;
            }
        }

        SessionUtil.getInstance().removeValue(req, "SHOPCART");
        SessionUtil.getInstance().putValue(req, "SHOPCART", shopcart);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getOutputStream(), 1);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        ProductModel model = HttpUtil.of(req.getReader()).toModel(ProductModel.class);

        List<ProductModel> shopcart = (List<ProductModel>) SessionUtil.getInstance().getValue(req, "SHOPCART");

        for (ProductModel item : shopcart) {
            if (item.getId() == model.getId()) {
                shopcart.remove(item);
                break;
            }
        }

        SessionUtil.getInstance().removeValue(req, "SHOPCART");
        SessionUtil.getInstance().putValue(req, "SHOPCART", shopcart);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getOutputStream(), 1);
    }
}
