package com.project.DuAnTotNghiep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Service
public class SessionService{
    private HttpSession session;

    public SessionService(HttpSession session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) session.getAttribute(name);
    }

    public <T> T get(String name, T defaultValue) {
        T value = get(name);
        return value != null ? value : defaultValue;
    }

    public void set(String name, Object value) {
        session.setAttribute(name, value);
    }

    public void remove(String name) {
        session.removeAttribute(name);
    }
}
