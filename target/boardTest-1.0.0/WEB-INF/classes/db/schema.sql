CREATE TABLE account (
    userId int(11) NOT NULL AUTO_INCREMENT,
    username varchar(30) NOT NULL,
    password varchar(2000) NOT NULL,
    userNickname varchar(20) NOT NULL,
    PRIMARY KEY (userId),
    UNIQUE KEY username (username)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;


CREATE TABLE account_role (
    username varchar(30) NOT NULL,
    role varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE board (
    boardKey int(11) NOT NULL,
    title varchar(200) NOT NULL,
    body text NOT NULL,
    userId int(11) NOT NULL,
    wdate datetime NOT NULL,
    fileId varchar(200) DEFAULT NULL,
    PRIMARY KEY (boardKey)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE comtecopseq (
    table_name varchar(60) COLLATE utf8_bin NOT NULL,
    next_id decimal(30,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='전자정부공용(수정금지)';

CREATE TABLE comtnfiledetail (
    atch_file_id char(20) COLLATE utf8_bin NOT NULL,
    file_sn decimal(10,0) NOT NULL,
    file_stre_cours varchar(6000) COLLATE utf8_bin NOT NULL,
    stre_file_nm varchar(765) COLLATE utf8_bin NOT NULL,
    orignl_file_nm varchar(765) COLLATE utf8_bin DEFAULT NULL,
    file_extsn varchar(60) COLLATE utf8_bin NOT NULL,
    file_cn varchar(255) COLLATE utf8_bin DEFAULT NULL,
    file_size decimal(20,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='전자정부공용(수정금지)';

CREATE TABLE comtnfile (
    atch_file_id char(20) COLLATE utf8_bin NOT NULL,
    creat_dt datetime NOT NULL,
    use_at char(1) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='전자정부공용(수정금지)';

INSERT INTO comtecopseq
(table_name,next_id)VALUES('FILE_ID', 11);
