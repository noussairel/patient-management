-- Ensure the 'users' table exists
CREATE TABLE IF NOT EXISTS "users" (
                                       id UUID PRIMARY KEY,
                                       email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

-- Insert the user if no existing user with the same id or email exists
INSERT INTO "users" (id, email, password, role)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'testuser@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '223e4567-e89b-12d3-a456-426614174006'
       OR email = 'testuser@test.com'
);

INSERT INTO "users" (id, email, password, role)
SELECT
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    'newuser@test.com',
    '$2b$10$xEVcLGLF0mm3uLq.GYl/x.APqlt9D3I5EkPP/bFZzOKrHu.pJ0JRu',
    'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM "users"
    WHERE id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
       OR email = 'newuser@test.com'
);


