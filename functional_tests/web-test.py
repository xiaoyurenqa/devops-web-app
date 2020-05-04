from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC 
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

import time
import pytest
import logging

base_url = 'http://3.22.185.164:8080/web-application/'

@pytest.fixture
def chrome_options(chrome_options, pytestconfig):
    chrome_options.add_argument('--headless')
    chrome_options.add_argument('--disable-dev-shm-usage')
    chrome_options.add_argument('--no-sandbox')
    return chrome_options

def test_page_title(selenium):
    selenium.implicitly_wait(15)
    selenium.maximize_window()
    selenium.get(base_url)
    assert 'Sample System' in selenium.title
    time.sleep(2)

def test_login_normal(selenium):
    test_page_title(selenium)
    selenium.find_element_by_name('username').send_keys('root')
    selenium.find_element_by_name('password').send_keys('password')
    selenium.find_element_by_xpath("//input[@type='submit' and @value='Login']").click()
    WebDriverWait(selenium, 10).until(
        EC.title_is("Logon success"),
        message="Error: The page does not jump to success page as expected"
    )
    assert selenium.current_url == base_url + "form_login"

def test_login_missing_username(selenium):
    test_page_title(selenium)
    selenium.find_element_by_name('username').send_keys('root')
    selenium.find_element_by_name('password').send_keys('')
    selenium.find_element_by_xpath("//input[@type='submit' and @value='Login']").click()
    WebDriverWait(selenium, 10).until(
        EC.title_is("Logon Sample System"),
        message="Error: The page does not stay the same as expected"
    )
    assert selenium.current_url == base_url + "form_login"     

def test_start_register(selenium):
    test_page_title(selenium)
    selenium.find_element_by_xpath("//a[@href='/web-application/register.jsp']").click()
    WebDriverWait(selenium, 10).until(
        EC.title_is("Register here"),
        message="Error: The page does not jump to registration page as expected"
    )

def test_register_normal(selenium):
    test_page_title(selenium)
    test_start_register(selenium)
    selenium.find_element_by_name('username').send_keys('root')
    selenium.find_element_by_name('password').send_keys('password')
    selenium.find_element_by_name('first_name').send_keys('Joe')
    selenium.find_element_by_name('last_name').send_keys('Sky')
    selenium.find_element_by_name('address').send_keys('KA Street P.O.Box 3202')
    selenium.find_element_by_name('contact').send_keys('13503829384')

    selenium.find_element_by_xpath("//input[@type='submit' and @value='Submit']").click()
    WebDriverWait(selenium, 10).until(
        EC.title_is("Logon Sample System"),
        message="Error: The page does not jump to success page as expected"
    )
    assert selenium.current_url == base_url + "form_register"

def test_register_missing_username(selenium):
    test_page_title(selenium)
    test_start_register(selenium)
    selenium.find_element_by_name('username').send_keys('')
    selenium.find_element_by_name('password').send_keys('password')
    selenium.find_element_by_name('first_name').send_keys('Joe')
    selenium.find_element_by_name('last_name').send_keys('Sky')
    selenium.find_element_by_name('address').send_keys('KA Street P.O.Box 3202')
    selenium.find_element_by_name('contact').send_keys('13503829384')

    selenium.find_element_by_xpath("//input[@type='submit' and @value='Submit']").click()
    WebDriverWait(selenium, 10).until(
        EC.title_is("Register here"),
        message="Error: The page does not jump to success page as expected"
    )
    assert selenium.current_url == base_url + "form_register"

#@pytest.mark.skip(reason="no way of currently testing this")
def test_register_special_username(selenium):
    test_page_title(selenium)
    test_start_register(selenium)
    selenium.find_element_by_name('username').send_keys('Denver')
    selenium.find_element_by_name('password').send_keys('password')
    selenium.find_element_by_name('first_name').send_keys('Joe')
    selenium.find_element_by_name('last_name').send_keys('Sky')
    selenium.find_element_by_name('address').send_keys('KA StreetP.@.Box 3202')
    selenium.find_element_by_name('contact').send_keys('13503829384')

    selenium.find_element_by_xpath("//input[@type='submit' and @value='Submit']").click()
    WebDriverWait(selenium, 10).until(
        EC.title_is("Logon Sample System"),
        message="Error: The page does not jump to success page as expected"
    )
    assert selenium.current_url == base_url + "form_register" 
