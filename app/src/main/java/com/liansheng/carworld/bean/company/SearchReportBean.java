package com.liansheng.carworld.bean.company;

import java.util.List;

public class SearchReportBean
{

    private String userId;
    private String order_no;
    private String vin;
    private String drivingLicense;
    private Object dr_vehicle_url;
    private ReportBean report;
    private boolean record;
    private int ret_code;
    private double price;
    private boolean paid;
    private String status;
    private String id;
    private String creation;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public Object getDr_vehicle_url() {
        return dr_vehicle_url;
    }

    public void setDr_vehicle_url(Object dr_vehicle_url) {
        this.dr_vehicle_url = dr_vehicle_url;
    }

    public ReportBean getReport() {
        return report;
    }

    public void setReport(ReportBean report) {
        this.report = report;
    }

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public static class ReportBean {
        private String order_no;
        private String createtime;
        private String vin;
        private String brand_img;
        private String maker;
        private String brand;
        private String series;
        private String last_time_to_shop;
        private String total_mileage;
        private OverviewBean overview;
        private List<RecordsBean> records;
        private String report_url;
        private boolean is_accident;
        private String is_accident_reason;
        private Object accident_grade;
        private Object category;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getBrand_img() {
            return brand_img;
        }

        public void setBrand_img(String brand_img) {
            this.brand_img = brand_img;
        }

        public String getMaker() {
            return maker;
        }

        public void setMaker(String maker) {
            this.maker = maker;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getSeries() {
            return series;
        }

        public void setSeries(String series) {
            this.series = series;
        }

        public String getLast_time_to_shop() {
            return last_time_to_shop;
        }

        public void setLast_time_to_shop(String last_time_to_shop) {
            this.last_time_to_shop = last_time_to_shop;
        }

        public String getTotal_mileage() {
            return total_mileage;
        }

        public void setTotal_mileage(String total_mileage) {
            this.total_mileage = total_mileage;
        }

        public OverviewBean getOverview() {
            return overview;
        }

        public void setOverview(OverviewBean overview) {
            this.overview = overview;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public String getReport_url() {
            return report_url;
        }

        public void setReport_url(String report_url) {
            this.report_url = report_url;
        }

        public boolean isIs_accident() {
            return is_accident;
        }

        public void setIs_accident(boolean is_accident) {
            this.is_accident = is_accident;
        }

        public String getIs_accident_reason() {
            return is_accident_reason;
        }

        public void setIs_accident_reason(String is_accident_reason) {
            this.is_accident_reason = is_accident_reason;
        }

        public Object getAccident_grade() {
            return accident_grade;
        }

        public void setAccident_grade(Object accident_grade) {
            this.accident_grade = accident_grade;
        }

        public Object getCategory() {
            return category;
        }

        public void setCategory(Object category) {
            this.category = category;
        }

        public static class OverviewBean {
            private FrameBean frame;
            private AppearanceBean appearance;
            private AirbagBean airbag;
            private EngineBean engine;
            private FireBean fire;
            private WaterBean water;
            private RecallBean recall;
            private MileageBean mileage;
            private int avg_mileage;

            public FrameBean getFrame() {
                return frame;
            }

            public void setFrame(FrameBean frame) {
                this.frame = frame;
            }

            public AppearanceBean getAppearance() {
                return appearance;
            }

            public void setAppearance(AppearanceBean appearance) {
                this.appearance = appearance;
            }

            public AirbagBean getAirbag() {
                return airbag;
            }

            public void setAirbag(AirbagBean airbag) {
                this.airbag = airbag;
            }

            public EngineBean getEngine() {
                return engine;
            }

            public void setEngine(EngineBean engine) {
                this.engine = engine;
            }

            public FireBean getFire() {
                return fire;
            }

            public void setFire(FireBean fire) {
                this.fire = fire;
            }

            public WaterBean getWater() {
                return water;
            }

            public void setWater(WaterBean water) {
                this.water = water;
            }

            public RecallBean getRecall() {
                return recall;
            }

            public void setRecall(RecallBean recall) {
                this.recall = recall;
            }

            public MileageBean getMileage() {
                return mileage;
            }

            public void setMileage(MileageBean mileage) {
                this.mileage = mileage;
            }

            public int getAvg_mileage() {
                return avg_mileage;
            }

            public void setAvg_mileage(int avg_mileage) {
                this.avg_mileage = avg_mileage;
            }

            public static class FrameBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class AppearanceBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class AirbagBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class EngineBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class FireBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class WaterBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class RecallBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }

            public static class MileageBean {
                private List<?> is_exception;
                private List<?> label;
                private List<?> exceptions;

                public List<?> getIs_exception() {
                    return is_exception;
                }

                public void setIs_exception(List<?> is_exception) {
                    this.is_exception = is_exception;
                }

                public List<?> getLabel() {
                    return label;
                }

                public void setLabel(List<?> label) {
                    this.label = label;
                }

                public List<?> getExceptions() {
                    return exceptions;
                }

                public void setExceptions(List<?> exceptions) {
                    this.exceptions = exceptions;
                }
            }
        }

        public static class RecordsBean {
            private String date;
            private int mile;
            private String type;
            private String detail;
            private String other;
            private String remark;
            private int is_exception;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getMile() {
                return mile;
            }

            public void setMile(int mile) {
                this.mile = mile;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getOther() {
                return other;
            }

            public void setOther(String other) {
                this.other = other;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getIs_exception() {
                return is_exception;
            }

            public void setIs_exception(int is_exception) {
                this.is_exception = is_exception;
            }
        }
    }
}
