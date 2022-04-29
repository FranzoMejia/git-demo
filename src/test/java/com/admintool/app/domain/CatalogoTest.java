package com.admintool.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.admintool.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalogo.class);
        Catalogo catalogo1 = new Catalogo();
        catalogo1.setId(1L);
        Catalogo catalogo2 = new Catalogo();
        catalogo2.setId(catalogo1.getId());
        assertThat(catalogo1).isEqualTo(catalogo2);
        catalogo2.setId(2L);
        assertThat(catalogo1).isNotEqualTo(catalogo2);
        catalogo1.setId(null);
        assertThat(catalogo1).isNotEqualTo(catalogo2);
    }
}
